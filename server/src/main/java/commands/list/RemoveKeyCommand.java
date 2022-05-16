package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.LabWork;
import response.Response;

import java.util.Map;

/**
 * Команда удаления элементов из коллекции по его ключу
 */

public class RemoveKeyCommand extends CommandAbstract {

    public RemoveKeyCommand(){
        setTitle("remove_key");
        setDescription("remove_key null : удалить элемент из коллекции по его ключу");
    }

    @Override
    public Response execute(CommandFields commandFields) {


        Response response = new Response();
        response.command = "remove_key";
        response.type = Response.Type.INPUT;
        String[] commandSplited = commandFields.getCommand().split(" ");
        String key = "";

        if (commandSplited.length == 1) {
            response.status = Response.Status.ERROR;
            response.message = "Введите ключ: ";
        }
        else {
            key = commandSplited[1];
        }

        if (commandFields.getRequest().element != null){
            key = commandFields.getRequest().element.toString();
        }

        if (!key.isEmpty()){

            if (commandFields.getLabWorkDAO().getAll().containsKey(key)){
                commandFields.getLabWorkDAO().delete(key);
                response.argument = "Элмент удалён";
                response.type = Response.Type.TEXT;
                response.status = Response.Status.OK;
            }
            else {
                response.argument = "Элмент с таким ключом не найден";
                response.message = "Введите ключ: ";
                response.status = Response.Status.ERROR;
            }

        }


        return response;

    }
}