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

        LabWork labWork;

        Response response = new Response();
        response.command = "update";
        response.type = Response.Type.UPDATE;
        response.status = Response.Status.OK;
        LabWorkChecker checker = new LabWorkChecker();

        Map.Entry<String, LabWork> labWorkEntry = Map.entry("", new LabWork());


        if (id != null) {

            boolean isCorrectId = false;
            for (Map.Entry<String, LabWork> entry : commandFields.getLabWorkDAO().getAll().entrySet()) {
                if (entry.getValue().getId().equals(Integer.parseInt(id))) {
                    labWorkEntry = Map.entry(entry.getKey(), entry.getValue());
                    isCorrectId = true;
                    break;
                }
            }

            if (isCorrectId) {
                response.type = Response.Type.UPDATE;
                response.status = Response.Status.OK;
                response.argument = new ParserJSON().serializeElement(labWorkEntry);
            } else {
                response.status = Response.Status.ERROR;
                response.type = Response.Type.UPDATE;
                response.message = "Элемент с таким id не найден";
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
                    response.status = Response.Status.ERROR;
                    response.type = Response.Type.UPDATE;
                    response.message = "Вы не ввели id";
                    response.argument = new ParserJSON().serializeElement(labWorkEntry);
                }

                else if (type.equals("id")) {

                    boolean isCorrectId = false;
                    for (Map.Entry<String, LabWork> entry : commandFields.getLabWorkDAO().getAll().entrySet()) {
                        if (entry.getValue().getId().equals(Integer.parseInt(id))) {
                            labWorkEntry = Map.entry(entry.getKey(), entry.getValue());
                            isCorrectId = true;
                            break;
                        }
                    }


                    if (isCorrectId) {
                        id = labWorkEntry.getValue().getId().toString();
                        response.type = Response.Type.UPDATE;
                        response.status = Response.Status.OK;
                        response.argument = new ParserJSON().serializeElement(labWorkEntry);

                    }
                    else {
                        response.status = Response.Status.ERROR;
                        response.type = Response.Type.UPDATE;
                        labWorkEntry.setValue(new LabWork());
                        response.message = "Элемент с таким id не найден";
                        response.argument = new ParserJSON().serializeElement(labWorkEntry);
                    }

                }


                else if (type.equals("lab_work_entry")) {
                    boolean isCorrectId = false;
                    for (Map.Entry<String, LabWork> entry : commandFields.getLabWorkDAO().getAll().entrySet()) {
                        if (entry.getValue().getId().equals(labWorkEntry.getValue().getId())) {
                            isCorrectId = true;
                            break;
                        }
                    }

                    if (isCorrectId){
                        response.status = Response.Status.OK;
                        response.type = Response.Type.TEXT;
                        response.argument = "Элемент обновлён";
                        commandFields.getLabWorkDAO().update(labWorkEntry.getValue().getId(), labWorkEntry.getValue());
                    }
                    else {
                        response.status = Response.Status.ERROR;
                        response.type = Response.Type.UPDATE;
                        labWorkEntry.setValue(new LabWork());
                        response.message = "Элемент с таким id не найден";
                        response.argument = new ParserJSON().serializeElement(labWorkEntry);
                    }
                }


            } else {
                response.status = Response.Status.ERROR;
                response.type = Response.Type.UPDATE;
                response.message = "Вы не ввели id";
                response.argument = new ParserJSON().serializeElement(labWorkEntry);
            }
        }


        return response;

    }
}
