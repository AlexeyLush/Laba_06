package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.LabWork;
import response.Response;

import java.util.Map;

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

        String argument = "";
        try{
            for (Map.Entry<String, LabWork> entry : commandFields.getLabWorkDAO().getAll().entrySet()) {
                argument += String.format("\nКлюч: %s\n%s", entry.getKey(),entry.getValue().toString());
            }
            return new Response(Response.Status.OK, Response.Type.TEXT, argument);
        }
        catch (NullPointerException nullPointerException){
            return new Response(Response.Status.ERROR, Response.Type.TEXT, "Ошбика на сервере!");
        }

    }

}
