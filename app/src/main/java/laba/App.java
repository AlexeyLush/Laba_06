package laba;
import dao.LabWorkDAO;
import files.DataFileManager;
import files.ExecuteFileManager;
import io.ConsoleManager;
import models.LabWork;
import request.Request;
import response.Response;
import services.parsers.ParserJSON;

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

    static String trimZeros(String str) {
        int pos = str.indexOf(0);
        return pos == -1 ? str : str.substring(0, pos);
    }

    public static void main(String[] args) {

        ConsoleManager consoleManager = new ConsoleManager();
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
                Request request = new Request(command, null);
                String json = new ParserJSON().serializeElement(request);
                byte[] arr = json.getBytes(StandardCharsets.UTF_8);
                int len = arr.length;
                datagramPacket = new DatagramPacket(arr, len, host, port);

                try{
                    datagramSocket.send(datagramPacket);

                }
                catch (IOException e) {
                    consoleManager.error("Ошибка во время отправке данных");
                    break;
                }

                datagramSocket.setSoTimeout(4000);
                try {
                    byte[] buffer = new byte[65000];
                    datagramPacket = new DatagramPacket(buffer, buffer.length, host, port);
                    datagramSocket.receive(datagramPacket);
                    String responseString = new String(buffer, StandardCharsets.UTF_8);
                    Response response = new ParserJSON().deserializeResponse(trimZeros(responseString));
                    consoleManager.output(response.argument.toString());
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
