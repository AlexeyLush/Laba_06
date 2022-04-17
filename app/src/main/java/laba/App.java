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
    public static void run(Scanner scanner, String dataFileName, String tempFileName, ConsoleManager consoleManager, LabWorkDAO labWorkDAO, boolean isMainFile){

        if (!isMainFile){
            consoleManager.warning("Внимание! Программа не смогла получить доступ к основному файлу из-за ограничений. Программа будет работать с временным файлом");
        }
        DataFileManager dataFileManager = new DataFileManager(dataFileName, tempFileName, consoleManager, scanner, isMainFile);
        if (isMainFile){
            labWorkDAO.initialMap(dataFileManager.readMap(dataFileName, true, true));
        } else{
            labWorkDAO.initialMap(dataFileManager.readMap(tempFileName, true,true));
            dataFileManager = new DataFileManager(tempFileName, dataFileName, consoleManager, scanner, isMainFile);
        }


        ExecuteFileManager executeFileManager = new ExecuteFileManager(dataFileName, consoleManager);
        CommandsManager commandsManager = new CommandsManager(scanner, consoleManager, dataFileManager, executeFileManager);


        while (isRun){
            commandsManager.inputCommand(labWorkDAO);
        }
    }

    public static void main(String[] args) {

//
//        try {
//            String dataFileName = System.getenv("LABWORKS_FILE_PATH");
//            String tempFileName = String.format("%s/lab_works_temp.json", System.getenv("TEMP"));
//            boolean isMainFile = true;
//
//            File file = new File(dataFileName);
//            if (!file.canRead()){
//                if (!file.createNewFile()){
//                    isMainFile = false;
//                } else {
//                    file.delete();
//                }
//            }
//            if (dataFileName.trim().isEmpty() || tempFileName.trim().isEmpty()){
//                consoleManager.error("Ошибка настройки переменного окружения! Программа завершает работу...");
//                App.exit();
//            }
//            run(scanner, dataFileName, tempFileName, consoleManager, labWorkDAO, isMainFile);
//
//        } catch (IOException | NullPointerException e){
//            consoleManager.error("Ошбика при работе с файлами");
//        }

        ConsoleManager consoleManager = new ConsoleManager();
        Scanner scanner = new Scanner(System.in);
        DatagramSocket datagramSocket;
        DatagramPacket datagramPacket;
        InetAddress host;
        int port;

        while (true){
            try{
                String str = scanner.nextLine();
                byte[] arr = str.getBytes(StandardCharsets.UTF_8);
                int len = arr.length;
                datagramSocket = new DatagramSocket();
                host = InetAddress.getLocalHost();
                port = 6789;
                datagramPacket = new DatagramPacket(arr, len, host, port);
                datagramSocket.setSoTimeout(5000);

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
