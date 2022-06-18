package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import response.Response;

/**
 * Команда вывыда справки по доступным командам
 */

public class HelpCommand extends CommandAbstract {


    public HelpCommand(){
        setTitle("help");
        setDescription("help : вывести справку по доступным командам");
    }

    @Override
    public Response execute(CommandFields commandFields) {

        Response response = new Response();
        response.statusCode = 200;
        response.contentType = Response.Type.TEXT;
        response.argument = "";

        commandFields
                .getCommandsManager()
                .getCommandsList()
                .entrySet()
                .forEach(entry -> response.argument += String.format("%s%s",entry.getValue().getDescription(), System.lineSeparator()));

        return response;
    }
}
