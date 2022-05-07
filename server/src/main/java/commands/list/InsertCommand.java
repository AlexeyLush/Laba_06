package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.Difficulty;
import models.LabWork;
import request.Request;
import response.Response;
import services.checkers.LabWorkChecker;
import services.elementProcces.LabWorkProcess;
import services.parsers.ParserJSON;
import services.spliters.SplitCommandOnIdAndJSON;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Map;

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

        String[] splitCommand = new SplitCommandOnIdAndJSON().splitedCommand(commandFields.getCommand(), commandFields.getConsoleManager());
        String key = splitCommand[0];
        String json = splitCommand[1];

        LabWork labWork;

        Response response = new Response();
        response.type = Response.Type.INSERT;
        response.status = Response.Status.OK;
        LabWorkChecker checker = new LabWorkChecker();

        if (json != null) {

            labWork = new ParserJSON().deserializeLabWork(json);
            Map.Entry<String, LabWork> labWorkEntry;

            String keyCheck = checker.checkUserKey(key);

            if (commandFields.getLabWorkDAO().getAll().containsKey(key)){
                labWorkEntry = Map.entry("", labWork);
                response.message = "Этот ключ уже занят";
                response.status = Response.Status.ERROR;
                response.argument = new ParserJSON().serializeElement(labWorkEntry);
            }
            else {
                labWorkEntry = Map.entry(key, labWork);

                String name = checker.checkNamePerson(labWork.getName());
                Long coordX = checker.checkX(labWork.getCoordinates().getX().toString());
                Integer coordY = checker.checkY(labWork.getCoordinates().getY().toString());
                Float minimalPoint = checker.checkMinimalPoint(labWork.getMinimalPoint().toString());
                String description =checker.checkDescription(labWork.getDescription());
                Difficulty difficulty =checker.checkDifficulty(labWork.getDifficulty().toString());
                String authorName = checker.checkNamePerson(labWork.getAuthor().getName());
                Long authorWeight = checker.checkWeightPerson(labWork.getAuthor().getWeight().toString());
                String authorPassportId = checker.checkPassportIdPerson(labWork.getAuthor().getPassportID());

                if (keyCheck == null || name == null || coordX == null || coordY == null
                        || minimalPoint == null || description == null || difficulty == null
                        || authorName == null || authorWeight == null || authorPassportId == null) {
                    response.status = Response.Status.ERROR;
                    response.argument = new ParserJSON().serializeElement(labWorkEntry);
                }

                else {
                    response.status = Response.Status.OK;
                    response.type = Response.Type.TEXT;
                    response.argument = "Элемент добавлен";
                    labWork.setCreationDate(ZonedDateTime.now());
                }
            }

        } else {
            Request request = commandFields.getRequest();
            labWork = new LabWork();
            Map.Entry<String, LabWork> labWorkEntry;
            if (request.element != null){
                labWorkEntry = new ParserJSON().deserializeEntryLabWork(request.element.toString());
                if (labWorkEntry != null) {
                    labWork = labWorkEntry.getValue();
                    key = labWorkEntry.getKey();
                }
            }


            if (key != null){
                if (commandFields.getLabWorkDAO().getAll().containsKey(key)){
                    labWorkEntry = Map.entry("", labWork);
                    response.message = "Этот ключ уже занят";
                    response.status = Response.Status.ERROR;
                } else {

                    if (labWork != null){

                        labWorkEntry = Map.entry(key, labWork);

                        String keyCheck = checker.checkUserKey(key);
                        String name = checker.checkNamePerson(labWork.getName());
                        Long coordX = checker.checkX(labWork.getCoordinates().getX().toString());
                        Integer coordY = checker.checkY(labWork.getCoordinates().getY().toString());
                        Float minimalPoint = checker.checkMinimalPoint(labWork.getMinimalPoint().toString());
                        String description =checker.checkDescription(labWork.getDescription());
                        Difficulty difficulty =checker.checkDifficulty(labWork.getDifficulty().toString());
                        String authorName = checker.checkNamePerson(labWork.getAuthor().getName());
                        Long authorWeight = checker.checkWeightPerson(labWork.getAuthor().getWeight().toString());
                        String authorPassportId = checker.checkPassportIdPerson(labWork.getAuthor().getPassportID());

                        if (keyCheck == null || name == null || coordX == null || coordY == null
                                || minimalPoint == null || description == null || difficulty == null
                                || authorName == null || authorWeight == null || authorPassportId == null) {
                            response.status = Response.Status.ERROR;
                            response.argument = new ParserJSON().serializeElement(labWorkEntry);
                        } else {
                            response.status = Response.Status.OK;
                            response.type = Response.Type.TEXT;
                            labWork.setCreationDate(ZonedDateTime.now());
                        }
                    }
                    else {
                        labWorkEntry = Map.entry("", new LabWork());
                    }
                }
            }
            else {
                labWorkEntry = Map.entry("", labWork);
                response.status = Response.Status.ERROR;
            }
            response.argument = new ParserJSON().serializeElement(labWorkEntry);
        }

        commandFields.getConsoleManager().successfully("Команда insert успешно выполнена");

        if (response.status == Response.Status.OK){
            response.type = Response.Type.TEXT;
            response.argument = "Элемент добавлен";
            commandFields.getLabWorkDAO().create(key, labWork);
        }

        return response;



    }
}
