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
        String argument = "";
        for (Map.Entry<String, CommandAbstract> entry : commandFields.getCommandsManager().getCommandsList().entrySet()) {
            argument += String.format("%s%s",entry.getValue().getDescription(), System.lineSeparator());
        }
        return new Response(Response.Status.OK, Response.Type.TEXT, argument);
    }
}
