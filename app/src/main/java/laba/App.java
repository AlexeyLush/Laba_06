package laba;

import commands.interactive.ExecuteScriptInteractiveCommand;
import commands.interactive.InsertInteractiveCommand;
import commands.interactive.UpdateInteractiveCommand;
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

    public static void exit() {
        isRun = false;
    }

    private static DatagramPacket createDatagramPacket(String command, InetAddress host, int port) {

        Request request = new Request(command, null);
        String json = new ParserJSON().serializeElement(request);
        byte[] arr = json.getBytes(StandardCharsets.UTF_8);
        int len = arr.length;
        return new DatagramPacket(arr, len, host, port);
    }

    private static Response getResponse(DatagramPacket datagramPacket, DatagramSocket datagramSocket, byte[] buffer, InetAddress host, int port) {
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

    private static Request createRequestInsertCommand(ConsoleManager consoleManager, Scanner scanner, Response response) {
        InsertInteractiveCommand insertInteractiveCommand = new InsertInteractiveCommand();
        return insertInteractiveCommand.inputData(consoleManager, scanner, response);
    }

    private static Response getResponseInsertCommand(DatagramPacket datagramPacket, DatagramSocket datagramSocket, byte[] buffer, InetAddress host, int port, ConsoleManager consoleManager, Scanner scanner, Response response) {
        try {
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

    private static Request createRequestExecuteScriptCommand(ConsoleManager consoleManager, Scanner scanner, Response response, List<String> listExecuteFiles) {
        ExecuteScriptInteractiveCommand executeScriptInteractiveCommand = new ExecuteScriptInteractiveCommand();
        return executeScriptInteractiveCommand.inputData(consoleManager, scanner, response, listExecuteFiles);
    }

    private static Response getResponseExecuteCommand(DatagramPacket datagramPacket, DatagramSocket datagramSocket, ConsoleManager consoleManager, Scanner scanner, InetAddress host, int port, Response response, byte[] buffer, List<String> listFiles) {
        try {
            String jsonInsert = new ParserJSON().serializeElement(createRequestExecuteScriptCommand(consoleManager, scanner, response, listFiles));
            byte[] arrInsert = jsonInsert.getBytes(StandardCharsets.UTF_8);
            int lenInsert = arrInsert.length;
            datagramPacket = new DatagramPacket(arrInsert, lenInsert, host, port);
            datagramSocket.send(datagramPacket);

            return getResponse(datagramPacket, datagramSocket, buffer, host, port);
        } catch (IOException e) {
            return null;
        }
    }

    private static Request createRequestUpdateCommand(ConsoleManager consoleManager, Scanner scanner, Response response) {
        UpdateInteractiveCommand updateInteractiveCommand = new UpdateInteractiveCommand();
        return updateInteractiveCommand.inputData(consoleManager, scanner, response);
    }

    private static Response getResponseUpdateCommand(DatagramPacket datagramPacket, DatagramSocket datagramSocket, ConsoleManager consoleManager, Scanner scanner, InetAddress host, int port, Response response, byte[] buffer) {
        try {
            String jsonInsert = new ParserJSON().serializeElement(createRequestUpdateCommand(consoleManager, scanner, response));
            byte[] arrInsert = jsonInsert.getBytes(StandardCharsets.UTF_8);
            int lenInsert = arrInsert.length;
            datagramPacket = new DatagramPacket(arrInsert, lenInsert, host, port);
            datagramSocket.send(datagramPacket);

            return getResponse(datagramPacket, datagramSocket, buffer, host, port);
        } catch (IOException e) {
            return null;
        }
    }

    private static Integer runCommand(DatagramPacket datagramPacket, DatagramSocket datagramSocket,
                                      InetAddress host, int port, ConsoleManager consoleManager, Scanner scanner, List<String> listExecuteFiles) {
        try {
            byte[] buffer = new byte[65000];
            Response response = getResponse(datagramPacket, datagramSocket, buffer, host, port);
            if (response == null) {
                throw new IOException();
            }
            if (response.type == Response.Type.INSERT) {
                while (response.type == Response.Type.INSERT) {
                    response = getResponseInsertCommand(datagramPacket, datagramSocket, buffer, host, port, consoleManager, scanner, response);
                    if (response == null) {
                        throw new IOException();
                    }
                }
            }
            if (response.type == Response.Type.INPUT) {
                while (response.type == Response.Type.INPUT) {
                    response = getResponseExecuteCommand(datagramPacket, datagramSocket, consoleManager, scanner, host, port, response, buffer, listExecuteFiles);
                    if (response == null) {
                        throw new IOException();
                    }
                }

            }
            if (response.type == Response.Type.TEXT) {


                if (response.isWait) {
                    try {
                        while (response.isWait) {
                            datagramSocket.setSoTimeout(1000);
                            consoleManager.outputln(response.argument.toString());
                            response = getResponse(datagramPacket, datagramSocket, buffer, host, port);
                        }
                        consoleManager.outputln(response.argument.toString());
                    } catch (Exception exception) {
                        return 1;
                    }

                } else {
                    consoleManager.outputln(response.argument.toString());
                }

            }
            if (response.type == Response.Type.UPDATE) {
                while (response.type == Response.Type.UPDATE) {
                    response = getResponseUpdateCommand(datagramPacket, datagramSocket, consoleManager, scanner, host, port, response, buffer);
                    if (response == null) {
                        throw new IOException();
                    }
                }
            }
            if (response.type == Response.Type.LIST) {
                String fileName = response.argument.toString();
                File executeFile = new File(String.format("scripts/%s.txt", fileName));
                if (!listExecuteFiles.contains(fileName)) {
                    listExecuteFiles.add(fileName);
                    ExecuteFileManager executeFileManager = new ExecuteFileManager(executeFile.getAbsolutePath(), consoleManager);
                    List<String> scripts = executeFileManager.readFile();

                    for (String script : scripts) {
                        datagramPacket = createDatagramPacket(script, host, port);

                        try {
                            datagramSocket.send(datagramPacket);
                            if (runCommand(datagramPacket, datagramSocket, host, port, consoleManager, scanner, listExecuteFiles) == null) {
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


        } catch (IOException e) {
            consoleManager.error("Не удалось установить соединение с сервером");
            return null;
        }
        return 1;
    }

    public static void main(String[] args) {

        ConsoleManager consoleManager = new ConsoleManager(true, false);
        Scanner scanner = new Scanner(System.in);
        DatagramSocket datagramSocket;
        DatagramPacket datagramPacket;
        List<String> listExecuteFiles = new ArrayList<>();
        InetAddress host;
        int port;

        while (true) {
            try {
                datagramSocket = new DatagramSocket();
                host = InetAddress.getLocalHost();
                port = 6790;
                consoleManager.output("Введите команду (help - список всех команд): ");

                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("exit")) {
                    App.exit();
                    break;
                }
                datagramPacket = createDatagramPacket(command, host, port);

                try {
                    datagramSocket.send(datagramPacket);
                } catch (IOException e) {
                    consoleManager.error("Ошибка во время отправке данных");
                }

//                datagramSocket.setSoTimeout(4000);

                if (runCommand(datagramPacket, datagramSocket, host, port, consoleManager, scanner, listExecuteFiles) == null) {
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
