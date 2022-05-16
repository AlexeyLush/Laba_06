package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.Difficulty;
import models.LabWork;
import response.Response;
import services.checkers.LabWorkChecker;
import services.parsers.ParserJSON;
import services.spliters.SplitCommandOnIdAndJSON;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Команда удаления из коллекции всех элементы, превышающие заданный
 */

public class RemoveGreaterCommand extends CommandAbstract {

    public RemoveGreaterCommand() {
        setTitle("remove_greater");
        setDescription("remove_greater {element} : удалить из коллекции все элементы, превышающие заданный");
    }


    @Override
    public Response execute(CommandFields commandFields) {


        String[] splitCommand = new SplitCommandOnIdAndJSON().splitedCommand(commandFields.getCommand(), commandFields.getConsoleManager());

        String json = splitCommand[1];


        LabWork labWork;

        Response response = new Response();
        response.command = "remove_greater";
        response.type = Response.Type.INSERT;
        response.status = Response.Status.OK;
        LabWorkChecker checker = new LabWorkChecker();

        Map.Entry<String, LabWork> labWorkEntry;


        if (json != null) {

            labWork = new ParserJSON().deserializeLabWork(json);

            if (labWork != null) {

                String name = checker.checkNamePerson(labWork.getName());
                Long coordX = checker.checkX(labWork.getCoordinates().getX().toString());
                Integer coordY = checker.checkY(labWork.getCoordinates().getY().toString());
                Float minimalPoint = checker.checkMinimalPoint(labWork.getMinimalPoint().toString());
                String description = checker.checkDescription(labWork.getDescription());
                Difficulty difficulty = checker.checkDifficulty(labWork.getDifficulty().toString());
                String authorName = checker.checkNamePerson(labWork.getAuthor().getName());
                Long authorWeight = checker.checkWeightPerson(labWork.getAuthor().getWeight().toString());
                String authorPassportId = checker.checkPassportIdPerson(labWork.getAuthor().getPassportID());

                labWorkEntry = Map.entry("111", labWork);

                if (name == null || coordX == null || coordY == null
                        || minimalPoint == null || description == null || difficulty == null
                        || authorName == null || authorWeight == null || authorPassportId == null) {
                    response.status = Response.Status.ERROR;
                    response.argument = new ParserJSON().serializeElement(labWorkEntry);
                } else {


                    LabWork finalLabWork = labWork;
                    commandFields
                            .getLabWorkDAO()
                            .getAll()
                            .entrySet()
                            .stream()
                            .filter(entry -> finalLabWork.getDescription().length() < entry.getValue().getDescription().length())
                            .forEach(entry -> commandFields.getLabWorkDAO().delete(entry.getKey()));

                    response.type = Response.Type.TEXT;
                    response.status = Response.Status.OK;
                    response.argument = "Элементы, превышающие заданный, удалены";
                }
            }

            else {
                labWork = new LabWork();
                labWorkEntry = Map.entry("111", labWork);
                response.type = Response.Type.INSERT;
                response.argument = new ParserJSON().serializeElement(labWorkEntry);
            }



        } else {

            if (commandFields.getRequest().element != null) {

                labWorkEntry = new ParserJSON().deserializeEntryLabWork(commandFields.getRequest().element.toString());

                LabWork finalLabWork = labWorkEntry.getValue();
                commandFields
                        .getLabWorkDAO()
                        .getAll()
                        .entrySet()
                        .stream()
                        .filter(entry -> finalLabWork.getDescription().length() < entry.getValue().getDescription().length())
                        .forEach(entry -> commandFields.getLabWorkDAO().delete(entry.getKey()));

                response.type = Response.Type.TEXT;
                response.status = Response.Status.OK;
                response.argument = "Элементы, превышающие заданный, удалены";

            } else {

                labWork = new LabWork();
                labWorkEntry = Map.entry("111", labWork);
                response.type = Response.Type.INSERT;
                response.argument = new ParserJSON().serializeElement(labWorkEntry);
            }
        }

        return response;
    }
}
