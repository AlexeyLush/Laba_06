package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.LabWork;
import response.Response;

import java.util.Map;

/**
 * Команда вывова элементов, значение поля description которых больше заданного
 */

public class FilterGreaterThanDescriptionCommand extends CommandAbstract {

    public FilterGreaterThanDescriptionCommand() {
        setTitle("filter_greater_than_description");
        setDescription("filter_greater_than_description description : вывести элементы, значение поля description которых больше заданного");
    }

    @Override
    public Response execute(CommandFields commandFields) {

        Response response = new Response();
        response.command = "filter_greater_than_description";
        response.type = Response.Type.INPUT;
        String[] commandSplited = commandFields.getCommand().split(" ");
        String description = "";

        if (commandSplited.length == 1) {
            response.status = Response.Status.ERROR;
            response.message = "Введите описание работы: ";
        }
        else {
            description = commandSplited[1];
        }

        if (commandFields.getRequest().element != null){
            description = commandFields.getRequest().element.toString();
        }

        if (!description.isEmpty()){

            response.argument = "";
            response.type = Response.Type.TEXT;
            response.status = Response.Status.OK;
            for (Map.Entry<String, LabWork> entry : commandFields.getLabWorkDAO().getAll().entrySet()) {
                if (entry.getValue().getDescription().length() > description.length()){
                    response.argument += String.format("\nКлюч: %s\n%s", entry.getKey(),entry.getValue().toString());
                }
            }
        }


        return response;
    }
}
