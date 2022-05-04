package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import response.Response;

/**
 * Команда очистки коллекции
 */

public class ClearCommand extends CommandAbstract {

    public ClearCommand(){
        setTitle("clear");
        setDescription("clear: очистить коллекцию");
    }

    @Override
    public Response execute(CommandFields commandFields) {
        commandFields.getLabWorkDAO().clear();
        return new Response(Response.Status.OK, Response.Type.TEXT, "Коллекция очищена\n");
    }
}