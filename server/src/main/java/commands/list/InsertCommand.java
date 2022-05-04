package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.LabWork;
import response.Response;
import services.checkers.LabWorkChecker;
import services.elementProcces.LabWorkProcess;
import services.parsers.ParserJSON;
import services.spliters.SplitCommandOnIdAndJSON;

import java.time.ZonedDateTime;

/**
 * Команда добавления нового элемента с заданным ключом
 */

public class InsertCommand extends CommandAbstract {


    public InsertCommand() {
        setTitle("insert");
        setDescription("insert null {element}: добавить новый элемент с заданным ключом");
    }

    @Override
    public Response execute(CommandFields commandFields) {

        LabWorkProcess labWorkProcess = new LabWorkProcess(commandFields.getConsoleManager(), commandFields.getScanner());
        LabWorkChecker checker = new LabWorkChecker();
        LabWork labWork = new LabWork();

        String[] splitCommand = new SplitCommandOnIdAndJSON().splitedCommand(commandFields.getCommand(), commandFields.getConsoleManager());
        String key = splitCommand[0];
        String json = splitCommand[1];


        if (json != null) {
            labWork = new ParserJSON().deserializeElement(json, LabWork.class);
            labWork.setCreationDate(ZonedDateTime.now());
        }

        while (checker.checkUserKey(key) == null) {
            commandFields.getConsoleManager().output("Введите ключ: ");
            key = commandFields.getScanner().nextLine();
        }

        if (json != null){
            labWork = labWorkProcess.getProcessedElementWithError(labWork, checker);
        } else {
            labWork = labWorkProcess.getProcessedElement(labWork, checker);
        }

        commandFields.getConsoleManager().successfully("Команда insert успешно выполнена");

        return new Response(null, null, null);

    }
}
