package server;

import commands.CommandsManager;
import dao.map.LabWorkDAO;
import database.PostgresDatabase;
import io.ConsoleManager;
import models.User;
import request.Request;
import response.Response;
import service.token.TokenGenerator;
import services.parsers.ParserJSON;
import services.trim.TrimMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Server {

    private static boolean isRun = true;
    private static final int SIZE_BUFFER = 65_000;

    public static void exit() {
        isRun = false;
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
            datagramChannel.send(buffer, address);
        } catch (IOException e) {
            consoleManager.error("Ошибка во время работы сервера");
        }

    }

    public static void run(PostgresDatabase database, ConsoleManager consoleManager, Scanner scanner, int port, List<String> tokens) {

        CommandsManager commandsManager = new CommandsManager(scanner, consoleManager, database);
        SocketAddress address = new InetSocketAddress(port);
        DatagramChannel datagramChannel = openChannel(address, consoleManager);

        consoleManager.successfully("Серевер запущен!");

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

                if (request.authorization == null) {

                    Response response = new Response();

                    if (request.message.equalsIgnoreCase("login") || request.message.equalsIgnoreCase("register")){
                        response.argument = request.message;
                        response.statusCode = 200;
                    }

                    else if (request.message.equalsIgnoreCase("Register_Account")) {
                        User user = new ParserJSON().deserializeUser(request.element.toString());
                        int id = database.getUserDAO().insert(user);
                        if (id == -1) {
                            response.message = "Этот логин уже занят. Попробуйте ещё раз";
                            response.statusCode = 400;
                        }
                        else if (id == 0) {
                            response.message = "Произошла ошибка на сервере. Попробуйте позже";
                            response.statusCode = 500;
                        }
                        else {
                            response.message = "Аккаунт успешно зарегистрирован. Войдите в него, чтобы получить " +
                                    "доступ к приложению.";
                            response.statusCode = 200;
                        }
                    }

                    else if (request.message.equalsIgnoreCase("Get_Salt")){
                        User user = new User();
                        user.login = request.element.toString();
                        String salt = database.getUserDAO().getSaltOfUser(user);
                        if (salt == null){
                            response.message = "Неверно введён логин или пароль";
                            response.statusCode = 400;
                        }
                        else {
                            response.argument = salt;
                            response.statusCode = 200;
                        }
                    }

                    else if (request.message.equalsIgnoreCase("Login_Account")) {
                        User user = new ParserJSON().deserializeUser(request.element.toString());
                        if (database.getUserDAO().login(user) == 1){
                            String token = TokenGenerator.generateToken(user.login, user.hash);
                            response.statusCode = 200;
                            response.message = "Добро пожаловать!";
                            response.argument = token;
                            if (!tokens.contains(token)){
                                tokens.add(token);
                            }
                        }
                        else if (database.getUserDAO().login(user) == -1) {
                            response.statusCode = 400;
                            response.message = "Неверно введён логин или пароль";
                        }
                        else {
                            response.statusCode = 500;
                            response.message = "Во время работы произошла ошибка. Поробуйте позже";
                        }
                    }


                    byte[] responseByte = createResponseByte(response);
                    buffer.flip();
                    buffer = ByteBuffer.wrap(responseByte);
                    sendMessage(buffer, address, datagramChannel, consoleManager);

                }

                else {

                    if (tokens.contains(request.authorization)){
                        Response response = commandsManager.inputCommand(request);

                        if (request.message.equals("exit")){
                            try{
                                tokens.remove(request.authorization);
                            }
                            catch (Exception ignored){

                            }
                        }

                        String responseJson = new ParserJSON().serializeElement(response);

                        int responseCountBytes = responseJson.getBytes(StandardCharsets.UTF_8).length;
                        if (responseCountBytes >= SIZE_BUFFER){

                            int startIndex = 0;
                            for (int i = 0; i < responseCountBytes / (SIZE_BUFFER - 4000); i++){

                                Response packetResponse = new Response();
                                packetResponse.contentType = Response.Type.TEXT;
                                packetResponse.statusCode = 206;

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
                                    packetResponse.statusCode = 200;
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

                    }

                    else {
                        Response response = new Response();
                        response.message = "Что-то пошло не так и мы не можем потвердить ваш аккаунт. Пожалуйста, " +
                                "перезайдите в систему";
                        response.statusCode = 409;
                        response.contentType = Response.Type.TEXT;
                        response.argument = "";

                        byte[] responseByte = createResponseByte(response);
                        buffer.flip();
                        buffer = ByteBuffer.wrap(responseByte);
                        sendMessage(buffer, address, datagramChannel, consoleManager);


                    }


                }


            }

            catch (Exception e){
                consoleManager.error("Ошибка во время работы сервера");
            }


        }

    }

    public static void main(String[] args) {

        ConsoleManager consoleManager = new ConsoleManager(true, true);

        Scanner scanner = new Scanner(System.in);
        PostgresDatabase postgresDatabase = new PostgresDatabase(consoleManager);
        postgresDatabase.getLabWorkDAO().setLabWorksFromDatabase();
        postgresDatabase.createDatabase();

        List<String> tokens = new ArrayList<>();

        if (isRun) {
            int port = 6790;
            run(postgresDatabase, consoleManager, scanner, port, tokens);
        }


    }
}