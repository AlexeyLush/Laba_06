package laba;

import interactive.ExecuteScriptInteractiveCommand;
import interactive.InputInteractiveCommand;
import interactive.InsertInteractiveCommand;
import interactive.UpdateInteractiveCommand;
import files.ExecuteFileManager;
import interactive.InputType;
import interactive.LoginInteractiveCommand;
import interactive.NotLogInInteractive;
import interactive.RegisterInteractive;
import io.ConsoleManager;
import models.User;
import request.Request;
import response.Response;
import services.hashing.HashPassword;
import services.parsers.ParserJSON;
import services.trim.TrimMessage;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Класс старта программы)
 */

public class App {

    private static boolean isRun = true;

    public static void exit() {
        isRun = false;
    }

    private static DatagramPacket createDatagramPacket(String authorization, String command, InetAddress host, int port) {
        Request request = new Request(authorization, "POST", "localhost", "command", command.length(), command, null);
        String json = new ParserJSON().serializeElement(request);
        byte[] arr = json.getBytes(StandardCharsets.UTF_8);
        int len = arr.length;
        return new DatagramPacket(arr, len, host, port);
    }

    private static DatagramPacket createDatagramPacket(Request request, InetAddress host, int port){
        String json = new ParserJSON().serializeElement(request);
        byte[] arr = json.getBytes(StandardCharsets.UTF_8);
        int len = arr.length;
        return new DatagramPacket(arr, len, host, port);
    }

    private static Response getResponse(DatagramSocket datagramSocket, byte[] buffer, InetAddress host, int port) {
        try {
            TrimMessage trimMessage = new TrimMessage();
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, host, port);
            datagramSocket.receive(datagramPacket);
            String responseString = new String(buffer, StandardCharsets.UTF_8);
            return new ParserJSON().deserializeResponse(trimMessage.trimZero(responseString));
        } catch (IOException e) {
            return null;
        }
    }

    private static Request createRequestInsertCommand(String authorization, ConsoleManager consoleManager, Scanner scanner, Response response) {
        InsertInteractiveCommand insertInteractiveCommand = new InsertInteractiveCommand();
        return insertInteractiveCommand.inputData(authorization, consoleManager, scanner, response);
    }

    private static Response getResponseInsertCommand(String authorization,DatagramSocket datagramSocket, byte[] buffer, InetAddress host, int port, ConsoleManager consoleManager, Scanner scanner, Response response) {
        try {
            String jsonInsert = new ParserJSON().serializeElement(createRequestInsertCommand(authorization, consoleManager, scanner, response));
            byte[] arrInsert = jsonInsert.getBytes(StandardCharsets.UTF_8);
            int lenInsert = arrInsert.length;
            DatagramPacket datagramPacket = new DatagramPacket(arrInsert, lenInsert, host, port);
            datagramSocket.send(datagramPacket);

            return getResponse(datagramSocket, buffer, host, port);
        } catch (IOException e) {
            return null;
        }
    }

    private static Request createRequestExecuteScriptCommand(String authorization, ConsoleManager consoleManager, Scanner scanner, Response response, List<String> listExecuteFiles) {
        ExecuteScriptInteractiveCommand executeScriptInteractiveCommand = new ExecuteScriptInteractiveCommand();
        return executeScriptInteractiveCommand.inputData(authorization, consoleManager, scanner, response, listExecuteFiles);
    }

    private static Response getResponseExecuteCommand(String authorization,DatagramSocket datagramSocket, ConsoleManager consoleManager, Scanner scanner, InetAddress host, int port, Response response, byte[] buffer, List<String> listFiles) {
        try {
            String jsonInsert = new ParserJSON().serializeElement(createRequestExecuteScriptCommand(authorization, consoleManager, scanner, response, listFiles));
            byte[] arrInsert = jsonInsert.getBytes(StandardCharsets.UTF_8);
            int lenInsert = arrInsert.length;
            DatagramPacket datagramPacket = new DatagramPacket(arrInsert, lenInsert, host, port);
            datagramSocket.send(datagramPacket);

            return getResponse(datagramSocket, buffer, host, port);
        } catch (IOException e) {
            return null;
        }
    }

    private static Request createRequestUpdateCommand(String authorization, ConsoleManager consoleManager, Scanner scanner, Response response) {
        UpdateInteractiveCommand updateInteractiveCommand = new UpdateInteractiveCommand();
        return updateInteractiveCommand.inputData(authorization,consoleManager, scanner, response);
    }

    private static Response getResponseUpdateCommand(String authorization, DatagramSocket datagramSocket, ConsoleManager consoleManager, Scanner scanner, InetAddress host, int port, Response response, byte[] buffer) {
        try {
            String jsonInsert = new ParserJSON().serializeElement(createRequestUpdateCommand(authorization, consoleManager, scanner, response));
            byte[] arrInsert = jsonInsert.getBytes(StandardCharsets.UTF_8);
            int lenInsert = arrInsert.length;
            DatagramPacket datagramPacket = new DatagramPacket(arrInsert, lenInsert, host, port);
            datagramSocket.send(datagramPacket);

            return getResponse(datagramSocket, buffer, host, port);
        } catch (IOException e) {
            return null;
        }
    }

    private static Request createRequestInputCommand(String authorization, ConsoleManager consoleManager, Scanner scanner, Response response){
        InputInteractiveCommand inputInteractiveCommand = new InputInteractiveCommand();
        return inputInteractiveCommand.inputData(authorization, consoleManager, scanner, response);
    }

    private static Response getResponseInputCommand(String authorization, DatagramSocket datagramSocket, ConsoleManager consoleManager, Scanner scanner, InetAddress host, int port, Response response, byte[] buffer) {
        try {
            String jsonInsert = new ParserJSON().serializeElement(createRequestInputCommand(authorization,consoleManager, scanner, response));
            byte[] arrInsert = jsonInsert.getBytes(StandardCharsets.UTF_8);
            int lenInsert = arrInsert.length;
            DatagramPacket datagramPacket = new DatagramPacket(arrInsert, lenInsert, host, port);
            datagramSocket.send(datagramPacket);
            return getResponse(datagramSocket, buffer, host, port);
        } catch (IOException e) {
            return null;
        }
    }


    private static Integer runCommand(String authorization, DatagramSocket datagramSocket,
                                      InetAddress host, int port, ConsoleManager consoleManager, Scanner scanner, List<String> listExecuteFiles) {
        try {
            byte[] buffer = new byte[65000];
            Response response = getResponse(datagramSocket, buffer, host, port);
            if (response == null) {
                throw new IOException();
            }
            if (response.contentType == Response.Type.INSERT) {
                while (response.contentType == Response.Type.INSERT) {
                    response = getResponseInsertCommand(authorization, datagramSocket, buffer, host, port, consoleManager, scanner, response);
                    if (response == null) {
                        throw new IOException();
                    }
                }
            }
            if (response.contentType == Response.Type.INPUT_SCRIPT) {
                while (response.contentType == Response.Type.INPUT_SCRIPT) {
                    response = getResponseExecuteCommand(authorization, datagramSocket, consoleManager, scanner, host, port, response, buffer, listExecuteFiles);
                    if (response == null) {
                        throw new IOException();
                    }
                }

            }

            if (response.contentType == Response.Type.INPUT) {
                while (response.contentType == Response.Type.INPUT) {
                    response = getResponseInputCommand(authorization,datagramSocket, consoleManager, scanner, host, port, response, buffer);
                    if (response == null) {
                        throw new IOException();
                    }
                }
            }

            if (response.contentType == Response.Type.TEXT) {

                if (response.statusCode == 206) {
                    outputMessage(response, consoleManager);
                        try {
                        while (response.statusCode == 206) {
                            outputMessage(response, consoleManager);
                            response = getResponse(datagramSocket, buffer, host, port);
                        }
                            outputMessage(response, consoleManager);
                    } catch (Exception exception) {
                        return 1;
                    }

                } else {
                    outputMessage(response, consoleManager);
                    if (response.argument != null) {
                        consoleManager.outputln(response.argument);
                    }
                }

            }
            if (response.contentType == Response.Type.UPDATE) {
                while (response.contentType == Response.Type.UPDATE) {
                    response = getResponseUpdateCommand(authorization, datagramSocket, consoleManager, scanner, host, port, response, buffer);
                    if (response == null) {
                        throw new IOException();
                    }
                }
            }
            if (response.contentType == Response.Type.LIST) {
                String fileName = response.argument.toString();
                File executeFile = new File(String.format("scripts/%s.txt", fileName));
                if (!listExecuteFiles.contains(fileName)) {
                    listExecuteFiles.add(fileName);
                    ExecuteFileManager executeFileManager = new ExecuteFileManager(executeFile.getAbsolutePath(), consoleManager);
                    List<String> scripts = executeFileManager.readFile();

                    for (String script : scripts) {
                        DatagramPacket datagramPacket = createDatagramPacket(authorization, script, host, port);

                        try {
                            datagramSocket.send(datagramPacket);
                            if (runCommand(authorization, datagramSocket, host, port, consoleManager, scanner, listExecuteFiles) == null) {
                                return null;
                            }
                        } catch (IOException e) {
                            consoleManager.error("Ошибка во время отправке данных");
                        }
                    }
                    listExecuteFiles.remove(fileName);

                } else {
                    consoleManager.warning(String.format("Файл %s уже был исполнен", fileName));
                }
            }

            if (response.statusCode == 409) {
                return 409;
            }


        } catch (IOException e) {
            consoleManager.error("Не удалось установить соединение с сервером. Попробуйте позже");
        }
        return 1;
    }

    private static void outputMessage(Response response, ConsoleManager consoleManager){
        if ((response.statusCode >= 500 && response.statusCode <= 599)
                || (response.statusCode >= 400 && response.statusCode <= 499)) {
            consoleManager.error(response.message);
        }
        else if (response.statusCode >= 200 && response.statusCode <= 299) {
            consoleManager.successfully(response.message);
        }
        else {
            consoleManager.outputln(response.message);
        }
    }
    public static void main(String[] args) {

        ConsoleManager consoleManager = new ConsoleManager(false, false);
        Scanner scanner = new Scanner(System.in);
        DatagramSocket datagramSocket;
        DatagramPacket datagramPacket;
        List<String> listExecuteFiles = new ArrayList<>();
        InetAddress host;
        int port;

        String authorization = null;

        while (true) {
            try {

                datagramSocket = new DatagramSocket();
                datagramSocket.setSoTimeout(5000);
                host = InetAddress.getByName("192.168.3.16");
                port = 6790;


                if (authorization != null) {
                    consoleManager.output("Введите команду (help - список всех команд): ");
                    String command = scanner.nextLine();
                    if (command.equalsIgnoreCase("exit")) {
                        App.exit();
                        datagramPacket = createDatagramPacket(authorization, command, host, port);
                        datagramSocket.send(datagramPacket);
                        break;
                    }
                    datagramPacket = createDatagramPacket(authorization, command, host, port);

                    try {
                        datagramSocket.send(datagramPacket);
                    } catch (IOException e) {
                        consoleManager.error("Ошибка во время отправке данных");
                    }

                    Integer code = runCommand(authorization, datagramSocket, host, port, consoleManager, scanner, listExecuteFiles);

                    if (code != null && code == 409){
                        authorization = null;
                    }
                }

                else {
                    NotLogInInteractive notLogInInteractive = new NotLogInInteractive();
                    InputType inputType = notLogInInteractive.inputData(consoleManager, scanner);

                    datagramSocket.setSoTimeout(5000);
                    byte[] buffer = new byte[65000];
                    if (inputType == InputType.EXIT){
                        break;
                    }
                    else {
                        DatagramPacket packet = createDatagramPacket(null, inputType.getType(), host, port);
                        datagramSocket.send(packet);
                        Response response = getResponse(datagramSocket, buffer, host, port);
                        if (response == null) {
                            throw new IOException();
                        }
                        if (response.statusCode == 200) {
                            Request request = null;
                            if (inputType == InputType.LOGIN){
                                LoginInteractiveCommand loginInteractiveCommand = new LoginInteractiveCommand();
                                request = loginInteractiveCommand.inputData(consoleManager, scanner);
                                request.authorization = null;
                                packet = createDatagramPacket(request, host, port);
                                datagramSocket.send(packet);
                                response = getResponse(datagramSocket, buffer, host, port);

                                if (response == null){
                                    throw new IOException();
                                }

                                outputMessage(response, consoleManager);
                                if (response.statusCode == 200) {
                                    String hash = HashPassword.getHashPassword(loginInteractiveCommand.getPassword(), response.argument.toString());
                                    User user = new User();
                                    user.login = loginInteractiveCommand.getUser().login;
                                    user.hash = hash;
                                    user.salt = response.argument.toString();

                                    request.element = new ParserJSON().serializeElement(user);
                                    request.message = InputType.LOGIN_ACCOUNT.getType();
                                    request.authorization = null;

                                    packet = createDatagramPacket(request, host, port);
                                    datagramSocket.send(packet);
                                    response = getResponse(datagramSocket, buffer, host, port);

                                    if (response == null){
                                        throw new IOException();
                                    }

                                    outputMessage(response, consoleManager);
                                    if (response.statusCode == 200) {
                                        authorization = response.argument.toString();
                                    }
                                }

                            }
                            else if (inputType == InputType.REGISTER){
                                RegisterInteractive registerInteractive = new RegisterInteractive();
                                request = registerInteractive.inputData(consoleManager, scanner);

                                request.authorization = null;
                                packet = createDatagramPacket(request, host, port);
                                datagramSocket.send(packet);
                                response = getResponse(datagramSocket, buffer, host, port);

                                if (response == null){
                                    throw new IOException();
                                }
                                outputMessage(response, consoleManager);
                            }

                        }
                    }
                }



            } catch (SocketException e) {
                consoleManager.error("Ошибка при создания сокета");
                break;
            } catch (UnknownHostException e) {
                consoleManager.error("Ошбика подключения к хосту");
                break;
            } catch (NoSuchElementException exception){
                consoleManager.outputln("Выход из программы...");
                break;
            } catch (IOException e) {
                consoleManager.error("Не удалось установить соединение с сервером. Попробуйте позже");
            }
        }


    }
}
