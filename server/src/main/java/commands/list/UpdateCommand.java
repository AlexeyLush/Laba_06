package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import io.ConsoleManager;
import models.Coordinates;
import models.Difficulty;
import models.LabWork;
import models.Person;
import request.Request;
import response.Response;
import services.checkers.LabWorkChecker;
import services.elementProcces.LabWorkProcess;
import services.parsers.ParserJSON;
import services.spliters.SplitCommandOnIdAndJSON;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

/**
 * Команда обновления значений элемента коллекции, id которого равен заданному
 */

public class UpdateCommand extends CommandAbstract {

    public UpdateCommand() {
        setTitle("update");
        setDescription("update id {element} : обновить значение элемента коллекции, id которого равен заданному");
    }

    @Override
    public Response execute(CommandFields commandFields) {

        String[] splitCommand = new SplitCommandOnIdAndJSON().splitedCommand(commandFields.getCommand(), commandFields.getConsoleManager());
        String id = splitCommand[0];
        String json = splitCommand[1];

        LabWork labWork = new LabWork();

        Response response = new Response();
        response.type = Response.Type.UPDATE;

        if (id == null && labWork.getId() == 0) {
            response.status = Response.Status.ERROR;
            labWork.setId(0);
            response.argument = new ParserJSON().serializeElement(labWork);
        } else {
            if (labWork.getId() > 0){
                id = labWork.getId().toString();
            }
            labWork = new LabWork();
            Integer idInt = Integer.parseInt(id);
            for (Map.Entry<String, LabWork> entry : commandFields.getLabWorkDAO().getAll().entrySet()) {
                if (entry.getValue().getId().equals(idInt)) {
                    labWork = entry.getValue();
                    break;
                }
            }

            if (labWork.getId() == null){
                response.status = Response.Status.ERROR;
                response.type = Response.Type.UPDATE;
                response.argument = "Вы не ввели id";
            }

            if (json != null) {

                LabWork labWorkTemp = new ParserJSON().deserializeLabWork(json);
                LabWorkChecker checker = new LabWorkChecker();

                String name = checker.checkNamePerson(labWorkTemp.getName());
                Long coordX = checker.checkX(labWorkTemp.getCoordinates().getX().toString());
                Integer coordY = checker.checkY(labWorkTemp.getCoordinates().getY().toString());
                Float minimalPoint = checker.checkMinimalPoint(labWorkTemp.getMinimalPoint().toString());
                String description = checker.checkDescription(labWorkTemp.getDescription());
                Difficulty difficulty = checker.checkDifficulty(labWorkTemp.getDifficulty().toString());
                String authorName = checker.checkNamePerson(labWorkTemp.getAuthor().getName());
                Long authorWeight = checker.checkWeightPerson(labWorkTemp.getAuthor().getWeight().toString());
                String authorPassportId = checker.checkPassportIdPerson(labWorkTemp.getAuthor().getPassportID());

                if (name == null || coordX == null || coordY == null
                        || minimalPoint == null || description == null || difficulty == null
                        || authorName == null || authorWeight == null || authorPassportId == null) {
                    response.status = Response.Status.ERROR;
                    response.argument = new ParserJSON().serializeElement(labWorkTemp);
                } else {
                    commandFields.getLabWorkDAO().update(idInt, labWorkTemp);
                    response.status = Response.Status.OK;
                    response.type = Response.Type.TEXT;
                    response.argument = "Элемент обновлён";
                }

            } else {
                response.status = Response.Status.ERROR;
                response.type = Response.Type.UPDATE;
                response.argument = labWork;
            }
        }


        return response;

    }
}
