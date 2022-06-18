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
        response.command = "insert";
        response.contentType = Response.Type.INSERT;
        response.statusCode = 200;
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
                    response.statusCode = 400;
                    response.argument = new ParserJSON().serializeElement(labWorkEntry);
                } else {

                    if (commandFields.getDatabase().getLabWorkDAO().getAll().containsKey(keyCheck)) {
                        labWorkEntry = Map.entry("", labWork);
                        response.message = "Этот ключ уже занят";
                        response.statusCode = 400;
                        response.argument = new ParserJSON().serializeElement(labWorkEntry);
                    }

                    else {
                        if (commandFields.getDatabase().getLabWorkDAO().create(labWorkEntry.getKey(), labWorkEntry.getValue(),
                                commandFields.getRequest().authorization) == 0){
                            labWork.setCreationDate(ZonedDateTime.now());
                            response.statusCode = 400;
                            response.contentType = Response.Type.TEXT;
                            response.message = "Во время добавления данных произошла ошибка";
                        }
                        else {
                            response.statusCode = 201;
                            response.contentType = Response.Type.TEXT;
                            response.message = "Элемент добавлен";
                            commandFields.getDatabase().getLabWorkDAO().setLabWorksFromDatabase();
                            commandFields.getConsoleManager().successfully("Команда insert успешно выполнена");
                        }

                    }
                }

            }
            else {
                labWorkEntry = Map.entry(key, labWork);
                response.statusCode = 400;
                response.argument = new ParserJSON().serializeElement(labWorkEntry);
            }

        } else {

            if (commandFields.getRequest().element != null) {

                labWorkEntry = new ParserJSON().deserializeEntryLabWork(commandFields.getRequest().element.toString());

                labWork = labWorkEntry.getValue();
                key = labWorkEntry.getKey();
                if (commandFields.getDatabase().getLabWorkDAO().getAll().containsKey(labWorkEntry.getKey())) {
                    labWorkEntry = Map.entry("", labWorkEntry.getValue());
                    response.message = "Этот ключ уже занят";
                    response.statusCode = 400;
                    response.argument = new ParserJSON().serializeElement(labWorkEntry);
                } else {

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
                        response.statusCode = 400;
                        response.argument = new ParserJSON().serializeElement(labWorkEntry);
                    } else {

                        if (commandFields.getDatabase().getLabWorkDAO().getAll().containsKey(keyCheck)) {
                            labWorkEntry = Map.entry("", labWork);
                            response.message = "Этот ключ уже занят";
                            response.statusCode = 400;
                            response.argument = new ParserJSON().serializeElement(labWorkEntry);
                        }

                        else {


                            if (commandFields.getDatabase().getLabWorkDAO().create(labWorkEntry.getKey(), labWorkEntry.getValue(), commandFields.getRequest().authorization) == 0){
                                labWork.setCreationDate(ZonedDateTime.now());
                                response.statusCode = 400;
                                response.contentType = Response.Type.TEXT;
                                response.message = "Во время добавления данных произошла ошибка";
                            }
                            else {
                                response.statusCode = 201;
                                response.contentType = Response.Type.TEXT;
                                response.message = "Элемент добавлен";
                                commandFields.getDatabase().getLabWorkDAO().setLabWorksFromDatabase();
                                commandFields.getConsoleManager().successfully("Команда insert успешно выполнена");
                            }

                        }
                    }
                }

            } else {

                labWork = new LabWork();

                if (json != null) {
                    labWork = new ParserJSON().deserializeLabWork(json);
                }
                labWorkEntry = Map.entry("", labWork);
                response.message = "Вы не ввели ключ";
                response.statusCode = 400;
                response.argument = new ParserJSON().serializeElement(labWorkEntry);
            }
        }

        return response;

    }
}
