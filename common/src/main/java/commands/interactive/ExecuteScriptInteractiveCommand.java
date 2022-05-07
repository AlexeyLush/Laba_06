package commands.interactive;

import commands.interactive.interfaces.InteractiveCommand;
import files.ExecuteFileManager;
import io.ConsoleManager;
import request.Request;
import response.Response;
import services.parsers.ParserJSON;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class ExecuteScriptInteractiveCommand {

    public Request inputData(ConsoleManager consoleManager, Scanner scanner, Response response, List<String> listExecuteFiles) {
        File executeFile = null;
        String fileName = "";
        Request request = new Request();
        request.commandName = "execute_script";
        if (response.status == Response.Status.ERROR){
            if (response.message != null){
                consoleManager.error(response.message);
            }

            consoleManager.output("Введите имя файла (файл должен находится в папке script): ");

            fileName = scanner.nextLine();

            executeFile = new File(String.format("scripts/%s.txt", fileName));

        }

        else if (response.status == Response.Status.OK){
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
