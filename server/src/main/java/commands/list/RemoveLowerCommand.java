package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.Difficulty;
import models.LabWork;
import response.Response;
import service.token.TokenGenerator;
import services.checkers.LabWorkChecker;
import services.parsers.ParserJSON;
import services.spliters.SplitCommandOnIdAndJSON;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * команда удаления из коллекции всех элементов, меньших, чем заданный
 */

public class RemoveLowerCommand extends CommandAbstract {

    public RemoveLowerCommand() {
        setTitle("remove_lower");
        setDescription("remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный");
    }


    @Override
    public Response execute(CommandFields commandFields) {
        String[] splitCommand = new SplitCommandOnIdAndJSON().splitedCommand(commandFields.getCommand(), commandFields.getConsoleManager());

        String json = splitCommand[1];


        LabWork labWork;

        Response response = new Response();
        response.command = "remove_lower";
        response.contentType = Response.Type.INSERT;
        response.statusCode = 200;
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
                    response.statusCode = 400;
                    response.argument = new ParserJSON().serializeElement(labWorkEntry);
                } else {

                    LabWork finalLabWork = labWork;
                    commandFields.getDatabase().getLabWorkDAO().initialMap(commandFields
                            .getDatabase().getLabWorkDAO()
                            .getAll()
                            .entrySet()
                            .stream()
                            .filter(entry -> finalLabWork.getDescription().length() > entry.getValue().getDescription().length())
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
                    commandFields
                            .getDatabase().getLabWorkDAO()
                            .getAll()
                            .forEach((key, value) -> commandFields.getDatabase().getLabWorkDAO().delete(key));
                    response.contentType = Response.Type.TEXT;
                    response.statusCode = 200;
                    response.argument = "Элементы меньшие, чем заданный, удалены";
                }
            }

            else {
                labWork = new LabWork();
                labWorkEntry = Map.entry("111", labWork);
                response.contentType = Response.Type.INSERT;
                response.argument = new ParserJSON().serializeElement(labWorkEntry);
            }



        } else {

            if (commandFields.getRequest().element != null) {

                String userName = TokenGenerator.decodeToken(commandFields.getRequest().authorization).userName;
                labWorkEntry = new ParserJSON().deserializeEntryLabWork(commandFields.getRequest().element.toString());

                LabWork finalLabWork = labWorkEntry.getValue();
                commandFields.getDatabase().getLabWorkDAO().initialMap(commandFields
                        .getDatabase().getLabWorkDAO()
                        .getAll()
                        .entrySet()
                        .stream()
                        .filter(entry -> userName.equals(entry.getValue().getUserName()))
                        .filter(entry -> finalLabWork.getDescription().length() > entry.getValue().getDescription().length())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
                commandFields
                        .getDatabase().getLabWorkDAO()
                        .getAll()
                        .forEach((key, value) -> commandFields.getDatabase().getLabWorkDAO().delete(key));

                response.contentType = Response.Type.TEXT;
                response.statusCode = 200;
                response.argument = "Элементы, превышающие заданный, удалены";
                commandFields.getDatabase().getLabWorkDAO().setLabWorksFromDatabase();

            } else {

                labWork = new LabWork();
                labWorkEntry = Map.entry("111", labWork);
                response.contentType = Response.Type.INSERT;
                response.argument = new ParserJSON().serializeElement(labWorkEntry);
            }
        }

        return response;
    }
}
