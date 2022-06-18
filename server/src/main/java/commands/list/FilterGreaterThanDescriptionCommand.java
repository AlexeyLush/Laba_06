package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import response.Response;

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
        response.contentType = Response.Type.INPUT;
        String[] commandSplited = commandFields.getCommand().split(" ");
        String description = "";

        if (commandSplited.length == 1) {
            response.statusCode = 400;
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
            response.contentType = Response.Type.TEXT;
            response.statusCode = 200;
            String finalDescription = description;
            commandFields
                    .getDatabase().getLabWorkDAO()
                    .getAll()
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().getDescription().length() > finalDescription.length())
                    .forEachOrdered(entry -> response.argument += String.format("\nКлюч: %s\n%s", entry.getKey(),entry.getValue().toString()));
        }


        return response;
    }
}
