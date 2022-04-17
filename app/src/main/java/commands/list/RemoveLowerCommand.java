package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import services.checkers.LabWorkChecker;
import services.elementProcces.LabWorkProcess;
import services.spliters.SplitCommandOnIdAndJSON;
import models.Coordinates;
import models.Difficulty;
import models.LabWork;
import models.Person;
import services.parsers.ParserJSON;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * команда удаления из коллекции всех элементов, меньших, чем заданный
 */

public class RemoveLowerCommand extends CommandAbstract {

    public RemoveLowerCommand() {
        setTitle("remove_lower");
        setDescription("remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный");
    }


    @Override
    public void execute(CommandFields commandFields) {

        LabWorkProcess labWorkProcess = new LabWorkProcess(commandFields.getConsoleManager(), commandFields.getScanner());
        LabWorkChecker checker = new LabWorkChecker();
        LabWork labWork = new LabWork();

        String[] splitCommand = new SplitCommandOnIdAndJSON().splitedCommand(commandFields.getCommand(), commandFields.getConsoleManager());
        String json = splitCommand[1];


        if (json != null) {
            labWork = new ParserJSON(commandFields.getConsoleManager()).deserializeElement(json);
            labWork.setCreationDate(ZonedDateTime.now());
            labWork = labWorkProcess.getProcessedElementWithError(labWork, checker);
        } else {
            labWork = labWorkProcess.getProcessedElement(labWork, checker);
        }

        for (Map.Entry<String, LabWork> entry : commandFields.getLabWorkDAO().getAll().entrySet()) {
            if (labWork.getDescription().length() > entry.getValue().getDescription().length()) {
                commandFields.getLabWorkDAO().delete(entry.getKey());
            }
        }
        commandFields.getConsoleManager().successfully("Команда remove_lower успешно выполнена");

    }
}
