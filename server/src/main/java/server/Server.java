package server;

import dao.LabWorkDAO;
import files.DataFileManager;
import io.ConsoleManager;
import org.checkerframework.checker.units.qual.C;

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
    public static void getDataFromFile(String dataFileName, String tempFileName, ConsoleManager consoleManager, Scanner scanner, LabWorkDAO labWorkDAO) {
        DataFileManager dataFileManager = new DataFileManager(dataFileName, tempFileName, consoleManager, scanner);
        String fileName = dataFileName;
        if (!dataFileManager.isMainFile()){
            fileName = tempFileName;
        }
        labWorkDAO.initialMap(dataFileManager.readMap(fileName,true, true));
    }
    public static void run(ConsoleManager consoleManager, int port ){
        try {
            consoleManager.successfully("Серевер запущен!");
            SocketAddress address = new InetSocketAddress(port);
            DatagramChannel datagramChannel = DatagramChannel.open();
            datagramChannel.bind(address);
            while (isRun) {
                byte[] arr = new byte[64];
                ByteBuffer buffer;
                buffer = ByteBuffer.wrap(arr);
                address = datagramChannel.receive(buffer);
                byte[] arr2 = buffer.array();

                System.out.println(new String(arr2, StandardCharsets.UTF_8));
                buffer.flip();
//                consoleManager.warning("Отправка данных");
//                datagramChannel.send(buffer, address);
//                consoleManager.successfully("Данные отправлены");
            }

        } catch (IOException e) {
            consoleManager.error("Ошибка во время открытия канала");
        }
    }
    public static void main(String[] args) {

        ConsoleManager consoleManager = new ConsoleManager(true);
        Scanner scanner = new Scanner(System.in);

        String dataFileName = System.getenv("LABWORKS_FILE_PATH");
        String tempFileName = String.format("%s/lab_works_temp.json", System.getenv("TEMP"));

        if (dataFileName.trim().isEmpty() || tempFileName.trim().isEmpty()) {
            consoleManager.error("Ошибка настройки переменного окружения! Программа завершает работу...");
            Server.exit();
        }

        if (isRun) {

            LabWorkDAO labWorkDAO = new LabWorkDAO();
            int port = 6790;

            getDataFromFile(dataFileName, tempFileName, consoleManager, scanner, labWorkDAO);
            run(consoleManager, port);
        }


    }
}
