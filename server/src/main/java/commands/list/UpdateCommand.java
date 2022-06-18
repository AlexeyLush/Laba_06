package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.LabWork;
import response.Response;
import service.token.TokenGenerator;
import services.checkers.LabWorkChecker;
import services.parsers.ParserJSON;
import services.spliters.SplitCommandOnIdAndJSON;

import java.util.Map;

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

        LabWork labWork;

        Response response = new Response();
        response.command = "update";
        response.contentType = Response.Type.UPDATE;
        response.statusCode = 200;
        LabWorkChecker checker = new LabWorkChecker();

        Map.Entry<String, LabWork> labWorkEntry = Map.entry("", new LabWork());


        if (id != null) {

            try {
                boolean isCorrectId = false;
                for (Map.Entry<String, LabWork> entry : commandFields.getDatabase().getLabWorkDAO().getAll().entrySet()) {
                    if (entry.getValue().getId().equals(Integer.parseInt(id))) {
                        labWorkEntry = Map.entry(entry.getKey(), entry.getValue());
                        isCorrectId = true;
                        break;
                    }
                }

                if (isCorrectId) {

                    String userNameOfToken = TokenGenerator.decodeToken(commandFields.getRequest().authorization).userName;
                    String userNameOfLabWork = labWorkEntry.getValue().getUserName();

                    if (userNameOfToken.equals(userNameOfLabWork)){
                        response.contentType = Response.Type.UPDATE;
                        response.statusCode = 200;
                        response.message = "Элемент обновлён";
                        response.argument = new ParserJSON().serializeElement(labWorkEntry);
                        commandFields.getDatabase().getLabWorkDAO().setLabWorksFromDatabase();
                    }

                    else {
                        response.statusCode = 400;
                        response.contentType = Response.Type.UPDATE;
                        response.message = "Вы можете обновлять только свои работы";
                        response.argument = new ParserJSON().serializeElement(labWorkEntry);
                    }

                } else {
                    response.statusCode = 400;
                    response.contentType = Response.Type.UPDATE;
                    response.message = "Элемент с таким id не найден";
                    response.argument = new ParserJSON().serializeElement(labWorkEntry);
                }
            } catch (NumberFormatException e){
                response.statusCode = 400;
                response.contentType = Response.Type.UPDATE;
                response.message = "Id должен быть числом";
                response.argument = new ParserJSON().serializeElement(labWorkEntry);
            }



        } else {
            if (commandFields.getRequest().element != null) {
                String type = "";
                try {
                    id = commandFields.getRequest().element.toString();
                    Integer.parseInt(id);
                    type = "id";
                } catch (NumberFormatException ignored){
                    labWorkEntry = new ParserJSON().deserializeEntryLabWork(commandFields.getRequest().element.toString());
                    if (labWorkEntry != null){
                        type = "lab_work_entry";
                    }
                }


                if (type.equals("")){
                    response.statusCode = 400;
                    response.contentType = Response.Type.UPDATE;
                    response.message = "Вы не ввели id";
                    response.argument = new ParserJSON().serializeElement(labWorkEntry);
                }

                else if (type.equals("id")) {

                    boolean isCorrectId = false;
                    for (Map.Entry<String, LabWork> entry : commandFields.getDatabase().getLabWorkDAO().getAll().entrySet()) {
                        if (entry.getValue().getId().equals(Integer.parseInt(id))) {
                            labWorkEntry = Map.entry(entry.getKey(), entry.getValue());
                            isCorrectId = true;
                            break;
                        }
                    }


                    if (isCorrectId) {
                        String userNameOfToken = TokenGenerator.decodeToken(commandFields.getRequest().authorization).userName;
                        String userNameOfLabWork = labWorkEntry.getValue().getUserName();

                        if (userNameOfToken.equals(userNameOfLabWork)){
                            response.contentType = Response.Type.UPDATE;
                            response.statusCode = 200;
                            response.argument = new ParserJSON().serializeElement(labWorkEntry);
                        }

                        else {
                            response.statusCode = 400;
                            response.contentType = Response.Type.UPDATE;
                            response.message = "Вы можете обновлять только свои работы";
                            response.argument = new ParserJSON().serializeElement(labWorkEntry);
                        }
                    }
                    else {
                        response.statusCode = 400;
                        response.contentType = Response.Type.UPDATE;
                        response.message = "Элемент с таким id не найден";
                        response.argument = new ParserJSON().serializeElement(labWorkEntry);
                    }

                }


                else if (type.equals("lab_work_entry")) {
                    boolean isCorrectId = false;
                    for (Map.Entry<String, LabWork> entry : commandFields.getDatabase().getLabWorkDAO().getAll().entrySet()) {
                        if (entry.getValue().getId().equals(labWorkEntry.getValue().getId())) {
                            isCorrectId = true;
                            break;
                        }
                    }

                    if (isCorrectId){
                        response.statusCode = 200;
                        response.contentType = Response.Type.TEXT;
                        response.message = "Элемент обновлён";
                        commandFields.getDatabase().getLabWorkDAO().update(labWorkEntry.getValue().getId(), labWorkEntry.getValue(), commandFields.getRequest().authorization);
                        commandFields.getDatabase().getLabWorkDAO().setLabWorksFromDatabase();
                    }
                    else {
                        response.statusCode = 400;
                        response.contentType = Response.Type.UPDATE;
                        response.message = "Элемент с таким id не найден";
                        response.argument = new ParserJSON().serializeElement(labWorkEntry);
                    }
                }


            } else {
                response.statusCode = 400;
                response.contentType = Response.Type.UPDATE;
                response.message = "Вы не ввели id";
                response.argument = new ParserJSON().serializeElement(labWorkEntry);
            }
        }


        return response;

    }
}
