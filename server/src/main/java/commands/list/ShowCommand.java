package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import response.Response;

/**
 * Команда вывода в стандарный поток всех элементов коллекции в строковом представлении
 */

public class ShowCommand extends CommandAbstract {

    public ShowCommand(){
        setTitle("show");
        setDescription("show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
    }

    @Override
    public Response execute(CommandFields commandFields) {

        try{

            Response response = new Response();
            response.statusCode = 200;
            response.contentType = Response.Type.TEXT;
            response.argument = "";

            commandFields
                    .getDatabase().getLabWorkDAO()
                    .getAll()
                    .entrySet()
                    .forEach(entry -> response.argument += String.format("\nКлюч: %s\n%s", entry.getKey(),entry.getValue().toString()));

            return response;
        }
        catch (NullPointerException nullPointerException){
            return new Response(500, Response.Type.TEXT, "", "Ошбика на сервере!", "show");
        }

    }

}
