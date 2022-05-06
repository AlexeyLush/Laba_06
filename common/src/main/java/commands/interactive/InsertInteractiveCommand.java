package commands.interactive;

import commands.interactive.interfaces.InteractiveCommand;
import io.ConsoleManager;
import models.LabWork;
import request.Request;
import response.Response;
import services.checkers.LabWorkChecker;
import services.elementProcces.LabWorkProcess;
import services.parsers.ParserJSON;

import java.util.Map;
import java.util.Scanner;

public class InsertInteractiveCommand implements InteractiveCommand {
    @Override
    public Request inputData(ConsoleManager consoleManager, Scanner scanner, Response response) {
        LabWorkProcess labWorkProcess = new LabWorkProcess(consoleManager, scanner);
        LabWorkChecker checker = new LabWorkChecker();

        Map.Entry<String, LabWork> entry = new ParserJSON().deserializeEntryLabWork(response.argument.toString());

        if (response.message != null){
            if (response.status == Response.Status.ERROR){
                consoleManager.error(response.message);
            } else if (response.status == Response.Status.OK){
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

        return new Request("insert", entry);

    }
}
