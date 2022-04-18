package laba;
import commands.CommandsManager;
import dao.LabWorkDAO;
import files.DataFileManager;
import files.ExecuteFileManager;
import io.ConsoleManager;
import models.LabWork;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Scanner;

/**
 * Класс старта программы)
 */

public class App {

    private static boolean isRun = true;
    public static void exit(){
        isRun = false;
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
                CommandsManager commandsManager = new CommandsManager(scanner, consoleManager);
                datagramSocket = new DatagramSocket();
                datagramSocket.setSoTimeout(5000);
                host = InetAddress.getLocalHost();
                port = 6790;
                commandsManager.inputCommand();
                byte[] arr = str.getBytes(StandardCharsets.UTF_8);
                int len = arr.length;
                datagramPacket = new DatagramPacket(arr, len, host, port);

                try{
                    datagramSocket.send(datagramPacket);
                }
                catch (IOException e) {
                    consoleManager.error("Ошибка во время отправке данных");
                    break;
                }

//                try {
//                    datagramPacket = new DatagramPacket(arr, len);
//                    datagramSocket.receive(datagramPacket);
//                } catch (IOException e) {
//                    consoleManager.error("Не удалось установить соединение с сервером");
//                    break;
//                }


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
