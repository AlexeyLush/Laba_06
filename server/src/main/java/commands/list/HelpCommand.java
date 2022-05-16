package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        response.status = Response.Status.OK;
        response.type = Response.Type.TEXT;
        response.argument = "";

        commandFields
                .getCommandsManager()
                .getCommandsList()
                .entrySet()
                .forEach(entry -> response.argument += String.format("%s%s",entry.getValue().getDescription(), System.lineSeparator()));

        return response;
    }
}
