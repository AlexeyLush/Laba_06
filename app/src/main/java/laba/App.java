package laba;
import commands.interactive.InsertInteractiveCommand;
import dao.LabWorkDAO;
import files.DataFileManager;
import files.ExecuteFileManager;
import io.ConsoleManager;
import models.LabWork;
import request.Request;
import response.Response;
import services.parsers.ParserJSON;
import services.trim.TrimMessage;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс старта программы)
 */

public class App {

    private static boolean isRun = true;
    public static void exit(){
        isRun = false;
    }

    private static DatagramPacket createDatagramPacket(String command, InetAddress host, int port){

        Request request = new Request(command, null);
        String json = new ParserJSON().serializeElement(request);
        byte[] arr = json.getBytes(StandardCharsets.UTF_8);
        int len = arr.length;
        return new DatagramPacket(arr, len, host, port);
    }
    private static Response getResponse(DatagramPacket datagramPacket, DatagramSocket datagramSocket, byte[] buffer, InetAddress host, int port){
        try {
            TrimMessage trimMessage = new TrimMessage();
            datagramPacket = new DatagramPacket(buffer, buffer.length, host, port);
            datagramSocket.receive(datagramPacket);
            String responseString = new String(buffer, StandardCharsets.UTF_8);
            return new ParserJSON().deserializeResponse(trimMessage.trimZero(responseString));
        } catch (IOException e) {
            return null;
        }
    }
    private static Request createRequestInsertCommand(ConsoleManager consoleManager, Scanner scanner, Response response){
        InsertInteractiveCommand insertInteractiveCommand = new InsertInteractiveCommand();
        return  insertInteractiveCommand.inputData(consoleManager, scanner, response);
    }
    private static Response getResponseInsertCommand(DatagramPacket datagramPacket, DatagramSocket datagramSocket, byte[] buffer, InetAddress host, int port, ConsoleManager consoleManager, Scanner scanner, Response response){
        try{
            String jsonInsert = new ParserJSON().serializeElement(createRequestInsertCommand(consoleManager, scanner, response));
            byte[] arrInsert = jsonInsert.getBytes(StandardCharsets.UTF_8);
            int lenInsert = arrInsert.length;
            datagramPacket = new DatagramPacket(arrInsert, lenInsert, host, port);
            datagramSocket.send(datagramPacket);

            return getResponse(datagramPacket, datagramSocket, buffer, host, port);
        } catch (IOException e) {
            return null;
        }
    }


    public static void main(String[] args) {

        ConsoleManager consoleManager = new ConsoleManager(true, false);
        Scanner scanner = new Scanner(System.in);
        DatagramSocket datagramSocket;
        DatagramPacket datagramPacket;
        InetAddress host;
        int port;

        while (true){
            try{
                datagramSocket = new DatagramSocket();
                host = InetAddress.getLocalHost();
                port = 6790;
                consoleManager.output("Введите команду (help - список всех команд): ");

                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("exit")){
                    App.exit();
                    break;
                }
                datagramPacket = createDatagramPacket(command, host, port);

                try{
                    datagramSocket.send(datagramPacket);
                }
                catch (IOException e) {
                    consoleManager.error("Ошибка во время отправке данных");
                }

//                datagramSocket.setSoTimeout(4000);
                try {
                    byte[] buffer = new byte[65000];
                    Response response = getResponse(datagramPacket, datagramSocket, buffer, host, port);
                    if (response == null){
                        throw new IOException();
                    }
                    if (response.type == Response.Type.INSERT){
                        while (response.type == Response.Type.INSERT){
                            response = getResponseInsertCommand(datagramPacket, datagramSocket, buffer, host, port, consoleManager, scanner, response);
                            if (response == null){
                                throw new IOException();
                            }
                        }
                    }
                    if (response.type == Response.Type.TEXT){
                        consoleManager.output(response.argument.toString());
                    }

                } catch (IOException e) {
                    consoleManager.error("Не удалось установить соединение с сервером");
                    break;
                }


            } catch (SocketException e) {
                consoleManager.error("Ошибка при создания сокета");
                break;
            } catch (UnknownHostException e) {
                consoleManager.error("Ошбика подключения к хосту");
                break;
            }
        }


    }
}
