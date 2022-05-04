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

public class Server {
    private static boolean isRun = true;

    public static void exit() {
        isRun = false;
    }

    public static void getDataFromFile(String dataFileName, String tempFileName, ConsoleManager consoleManager, Scanner scanner, LabWorkDAO labWorkDAO, DataFileManager dataFileManager) {
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

        consoleManager.successfully("Серевер запущен!");

        while (isRun) {

            byte[] arr = new byte[65000];
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
            Response response = commandsManager.inputCommand(request.commandName);
            byte[] responseByte = createResponseByte(response);

            buffer.flip();
            buffer = ByteBuffer.wrap(responseByte);
            sendMessage(buffer, address, datagramChannel, consoleManager);

        }

    }

    public static void main(String[] args) {

        ConsoleManager consoleManager = new ConsoleManager(true);

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

            getDataFromFile(dataFileName, tempFileName, consoleManager, scanner, labWorkDAO, dataFileManager);
            run(consoleManager, scanner, port, labWorkDAO, dataFileManager);
        }


    }
}
