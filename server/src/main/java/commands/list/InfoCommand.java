package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import response.Response;
import services.parsers.ParserJSON;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Команда вывода в стандартный поток информации о коллекции (тип, дата инициализации, количество элементов и т.д.
 */

public class InfoCommand extends CommandAbstract {

    public InfoCommand(){
        setTitle("info");
        setDescription("info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
    }

    @Override
    public Response execute(CommandFields commandFields) {

        String argument = commandFields.getDatabase().getLabWorkDAO().getAll().getClass() + "\n"
                + commandFields.getDatabase().getLabWorkDAO().getAll().size() + "\n";
        return new Response(200, Response.Type.TEXT, argument, "", "info");
    }
}
