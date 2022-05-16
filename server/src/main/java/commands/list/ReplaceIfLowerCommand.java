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
 * Команда замещения значений по ключу, если новое значение меньше старого
 */

public class ReplaceIfLowerCommand extends CommandAbstract {

    public ReplaceIfLowerCommand() {
        setTitle("replace_if_lower");
        setDescription("replace_if_lower null {element} : заменить значение по ключу, если новое значение меньше старого");
    }

    @Override
    public Response execute(CommandFields commandFields) {

        String[] splitCommand = new SplitCommandOnIdAndJSON().splitedCommand(commandFields.getCommand(), commandFields.getConsoleManager());


        String key = splitCommand[0];
        String json = splitCommand[1];

        LabWork labWork;

        Response response = new Response();
        response.command = "replace_if_lower";
        response.type = Response.Type.INSERT;
        response.status = Response.Status.OK;
        LabWorkChecker checker = new LabWorkChecker();

        Map.Entry<String, LabWork> labWorkEntry;

        if (key != null) {

            labWork = new LabWork();

            if (json != null) {

                labWork = new ParserJSON().deserializeLabWork(json);

                String keyCheck = checker.checkUserKey(key);
                String name = checker.checkNamePerson(labWork.getName());
                Long coordX = checker.checkX(labWork.getCoordinates().getX().toString());
                Integer coordY = checker.checkY(labWork.getCoordinates().getY().toString());
                Float minimalPoint = checker.checkMinimalPoint(labWork.getMinimalPoint().toString());
                String description = checker.checkDescription(labWork.getDescription());
                Difficulty difficulty = checker.checkDifficulty(labWork.getDifficulty().toString());
                String authorName = checker.checkNamePerson(labWork.getAuthor().getName());
                Long authorWeight = checker.checkWeightPerson(labWork.getAuthor().getWeight().toString());
                String authorPassportId = checker.checkPassportIdPerson(labWork.getAuthor().getPassportID());

                labWorkEntry = Map.entry(key, labWork);

                if (keyCheck == null || name == null || coordX == null || coordY == null
                        || minimalPoint == null || description == null || difficulty == null
                        || authorName == null || authorWeight == null || authorPassportId == null) {
                    response.status = Response.Status.ERROR;
                    response.argument = new ParserJSON().serializeElement(labWorkEntry);
                } else {

                    if (commandFields.getLabWorkDAO().getAll().containsKey(keyCheck)) {

                        response.status = Response.Status.OK;
                        response.type = Response.Type.TEXT;
                        response.argument = "Элемент изменён";
                        labWork.setCreationDate(ZonedDateTime.now());
                        if (labWorkEntry.getValue().getDescription().length() < commandFields.getLabWorkDAO().get(labWorkEntry.getKey()).getDescription().length()) {
                            commandFields.getLabWorkDAO().delete(labWorkEntry.getKey());
                        }
                        commandFields.getLabWorkDAO().create(labWorkEntry.getKey(), labWorkEntry.getValue());
                    }

                    else {
                        labWorkEntry = Map.entry("", labWork);
                        response.message = "Элемент с таким ключом не найден";
                        response.status = Response.Status.ERROR;
                        response.argument = new ParserJSON().serializeElement(labWorkEntry);
                    }
                }

            }
            else {
                labWorkEntry = Map.entry(key, labWork);
                if (commandFields.getLabWorkDAO().getAll().containsKey(labWorkEntry.getKey())) {
                    response.status = Response.Status.ERROR;
                    response.argument = new ParserJSON().serializeElement(labWorkEntry);
                }
                else {
                    response.message = "Элемент с таким ключом не найден";
                    response.status = Response.Status.ERROR;
                    labWorkEntry = Map.entry("", labWork);
                    response.argument = new ParserJSON().serializeElement(labWorkEntry);
                }
            }

        } else {

            if (commandFields.getRequest().element != null) {

                labWorkEntry = new ParserJSON().deserializeEntryLabWork(commandFields.getRequest().element.toString());

                if (commandFields.getLabWorkDAO().getAll().containsKey(labWorkEntry.getKey())) {
                    response.status = Response.Status.OK;
                    response.type = Response.Type.TEXT;
                    response.argument = "Элемент изменён";
                    LabWork tempLabWork = labWorkEntry.getValue();
                    tempLabWork.setCreationDate(ZonedDateTime.now());
                    labWorkEntry.setValue(tempLabWork);
                    commandFields.getLabWorkDAO().create(labWorkEntry.getKey(), labWorkEntry.getValue());
                } else {
                    labWorkEntry = Map.entry("", labWorkEntry.getValue());
                    response.message = "Элемент с таким ключом не найден";
                    response.status = Response.Status.ERROR;
                    response.argument = new ParserJSON().serializeElement(labWorkEntry);
                }

            } else {

                labWork = new LabWork();

                if (json != null) {
                    labWork = new ParserJSON().deserializeLabWork(json);
                }
                labWorkEntry = Map.entry("", labWork);
                response.message = "Вы не ввели ключ";
                response.status = Response.Status.ERROR;
                response.argument = new ParserJSON().serializeElement(labWorkEntry);
            }
        }

        return response;

    }
}
