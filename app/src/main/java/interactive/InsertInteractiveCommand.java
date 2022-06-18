package interactive;

import io.ConsoleManager;
import models.LabWork;
import request.Request;
import response.Response;
import services.checkers.LabWorkChecker;
import services.elementProcces.LabWorkProcess;
import services.parsers.ParserJSON;

import java.util.Map;
import java.util.Scanner;

public class InsertInteractiveCommand {

    public Request inputData(String authorization, ConsoleManager consoleManager, Scanner scanner, Response response) {
        LabWorkProcess labWorkProcess = new LabWorkProcess(consoleManager, scanner);
        LabWorkChecker checker = new LabWorkChecker();
        Map.Entry<String, LabWork> entry;

        entry = new ParserJSON().deserializeEntryLabWork(response.argument.toString());


        if (response.message != null){
            if (response.statusCode == 400){
                consoleManager.error(response.message);
            } else if (response.statusCode == 200){
                consoleManager.successfully(response.message);
            }
        }

        String key = entry.getKey();
        LabWork labWork = entry.getValue();


        while (checker.checkUserKey(key) == null){
            consoleManager.output("Введите ключ: ");
            key = scanner.nextLine();
            if (checker.checkUserKey(key) == null){
                consoleManager.error("Ключ не должен быть пустым и содержать пустые символы!");
            }
        }


        labWork = labWorkProcess.getProcessedElement(labWork, checker);
        entry = Map.entry(key, labWork);

        String json = new ParserJSON().serializeElement(entry);
        Request request = new Request(authorization,"POST", "localhost", "command", json.length(), response.command, json);
        return request;

    }
}
