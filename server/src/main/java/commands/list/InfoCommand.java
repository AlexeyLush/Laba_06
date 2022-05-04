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
        String date = new ParserJSON().getDataFromFile(commandFields.getDataFileManager().readFile());

        String argument = ZonedDateTime.parse(date).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + "\n"
                + commandFields.getLabWorkDAO().getAll().getClass() + "\n"
                + commandFields.getLabWorkDAO().getAll().size() + "\n";
        Response response = new Response(Response.Status.OK, Response.Type.TEXT, argument);
        return response;
    }
}
