package interactive;

import io.ConsoleManager;
import request.Request;
import response.Response;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class ExecuteScriptInteractiveCommand {

    public Request inputData(String authorization, ConsoleManager consoleManager, Scanner scanner, Response response, List<String> listExecuteFiles) {
        File executeFile = null;
        String fileName = "";
        Request request = new Request();
        request.authorization = authorization;
        request.message = "execute_script";
        if (response.statusCode == 400){
            if (response.message != null){
                consoleManager.error(response.message);
            }

            consoleManager.output("Введите имя файла (файл должен находится в папке script): ");

            fileName = scanner.nextLine();

            executeFile = new File(String.format("scripts/%s.txt", fileName));

        }

        else if (response.statusCode == 200){
            fileName = response.argument.toString();
        }


        if (!executeFile.isFile()){
            while (!executeFile.isFile()){
                consoleManager.error("Этот файл невозмножно отркыть");
                consoleManager.output("Введите имя файла (файл должен находится в папке script): ");
                fileName = scanner.nextLine();
                executeFile = new File(String.format("scripts/%s.txt", fileName));

            }
        }

        request.element = fileName;


        return request;
    }
}
