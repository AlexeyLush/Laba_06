package server;

import commands.CommandsManager;
import dao.LabWorkDAO;
import files.DataFileManager;
import io.ConsoleManager;
import org.checkerframework.checker.units.qual.C;
import request.Request;
import response.Response;
import services.parsers.ParserJSON;
import services.trim.TrimMessage;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Server {
    private static boolean isRun = true;
    private static final int SIZE_BUFFER = 65_000;

    public static void exit() {
        isRun = false;
    }

    public static void getDataFromFile(String dataFileName, String tempFileName, LabWorkDAO labWorkDAO, DataFileManager dataFileManager) {
        String fileName = dataFileName;
        if (!dataFileManager.isMainFile()) {
            fileName = tempFileName;
        }
        labWorkDAO.initialMap(dataFileManager.readMap(fileName, true, true));
    }

    private static DatagramChannel openChannel(SocketAddress address, ConsoleManager consoleManager) {
        try {
            DatagramChannel datagramChannel = DatagramChannel.open();
            datagramChannel.bind(address);
            return datagramChannel;
        } catch (IOException exception) {
            consoleManager.error("Ошибка во время работы сервера");
        }
        return null;
    }
    private static SocketAddress receiveDatagram(DatagramChannel datagramChannel, ByteBuffer buffer, ConsoleManager consoleManager) {
        try {
            return datagramChannel.receive(buffer);
        } catch (IOException e) {
            consoleManager.error("Ошибка во время работы сервера");
        }
        return null;
    }

    private static Request getRequest(ByteBuffer buffer) {

        TrimMessage trimMessage = new TrimMessage();
        byte[] arr2 = buffer.array();
        String requestString = new String(arr2, StandardCharsets.UTF_8);
        requestString = trimMessage.trimZero(requestString);

        return new ParserJSON().deserializeRequest(requestString, Request.class);
    }
    private static byte[] createResponseByte(Response response) {
        String responseJson = new ParserJSON().serializeElement(response);
        return responseJson.getBytes(StandardCharsets.UTF_8);
    }
    private static void sendMessage(ByteBuffer buffer, SocketAddress address, DatagramChannel datagramChannel, ConsoleManager consoleManager) {
        try {
            consoleManager.warning("Отправка данных");
            datagramChannel.socket().setSoTimeout(1000);
            datagramChannel.send(buffer, address);
            consoleManager.successfully("Данные отправлены");
        } catch (IOException e) {
            consoleManager.error("Ошибка во время работы сервера");
        }

    }

    public static void run(ConsoleManager consoleManager, Scanner scanner, int port, LabWorkDAO labWorkDAO, DataFileManager dataFileManager) {

        CommandsManager commandsManager = new CommandsManager(scanner, consoleManager, labWorkDAO, dataFileManager);
        SocketAddress address = new InetSocketAddress(port);
        DatagramChannel datagramChannel = openChannel(address, consoleManager);

        if (datagramChannel != null){
            getDataFromFile(dataFileManager.getFileName(), dataFileManager.getTempFileName(), labWorkDAO, dataFileManager);
            consoleManager.successfully("Серевер запущен!");
        }


        while (isRun) {

            try{
                byte[] arr = new byte[SIZE_BUFFER];
                ByteBuffer buffer;
                buffer = ByteBuffer.wrap(arr);


                if (datagramChannel == null) {
                    break;
                }
                address = receiveDatagram(datagramChannel, buffer, consoleManager);

                if (address == null) {
                    break;
                }


                Request request = getRequest(buffer);

                Response response = commandsManager.inputCommand(request);

                String responseJson = new ParserJSON().serializeElement(response);

                int responseCountBytes = responseJson.getBytes(StandardCharsets.UTF_8).length;
                if (responseCountBytes >= SIZE_BUFFER){


                    int startIndex = 0;
                    for (int i = 0; i < responseCountBytes / (SIZE_BUFFER - 4000); i++){

                        Response packetResponse = new Response();
                        packetResponse.status = Response.Status.OK;
                        packetResponse.type = Response.Type.TEXT;
                        packetResponse.isWait = true;

                        int sizePacketResponse = new ParserJSON().serializeElement(new ParserJSON().serializeElement(packetResponse).getBytes(StandardCharsets.UTF_8)).length();

                        int sizeResponseArgument = response.argument.toString().getBytes(StandardCharsets.UTF_8).length;
                        int difference = sizeResponseArgument - (i + 1) * (SIZE_BUFFER - 4000) - sizePacketResponse;
                        if (difference <= 0){
                            packetResponse.argument = Arrays.copyOfRange(response.argument.toString().getBytes(StandardCharsets.UTF_8), startIndex, sizeResponseArgument);
                        }
                        else {
                            packetResponse.argument = Arrays.copyOfRange(response.argument.toString().getBytes(StandardCharsets.UTF_8), startIndex, (i + 1) * (SIZE_BUFFER - 4000) - sizePacketResponse);
                            startIndex = (i + 1) * (SIZE_BUFFER - 4000) - sizePacketResponse;
                        }

                        packetResponse.argument = new String((byte[]) packetResponse.argument, StandardCharsets.UTF_8);

                        if (i + 1 >= responseCountBytes / (SIZE_BUFFER - 4000)){
                            packetResponse.isWait = false;
                        }
                        byte[] responseByte = createResponseByte(packetResponse);
                        buffer.flip();
                        buffer = ByteBuffer.wrap(responseByte);

                        TimeUnit.MILLISECONDS.sleep(10);

                        sendMessage(buffer, address, datagramChannel, consoleManager);
                    }
                }

                else {
                    byte[] responseByte = createResponseByte(response);

                    buffer.flip();
                    buffer = ByteBuffer.wrap(responseByte);

                    sendMessage(buffer, address, datagramChannel, consoleManager);

                }

                dataFileManager.save(labWorkDAO.getAll());
            }

            catch (Exception e){
                consoleManager.error("Ошибка во время работы сервера");
            }


        }

    }

    public static void main(String[] args) {

        ConsoleManager consoleManager = new ConsoleManager(true, true);

        Scanner scanner = new Scanner(System.in);

        String dataFileName = System.getenv("LABWORKS_FILE_PATH");
        String tempFileName = String.format("%s/lab_works_temp.json", System.getenv("TEMP"));
        DataFileManager dataFileManager = new DataFileManager(dataFileName, tempFileName, consoleManager, scanner);
        if (dataFileName.trim().isEmpty() || tempFileName.trim().isEmpty()) {
            consoleManager.error("Ошибка настройки переменного окружения! Программа завершает работу...");
            Server.exit();
        }

        if (isRun) {
            LabWorkDAO labWorkDAO = new LabWorkDAO();
            int port = 6790;
            run(consoleManager, scanner, port, labWorkDAO, dataFileManager);
        }


    }
}