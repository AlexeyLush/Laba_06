package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.LabWork;
import response.Response;
import service.token.TokenGenerator;

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
        response.contentType = Response.Type.INPUT;
        String[] commandSplited = commandFields.getCommand().split(" ");
        String key = "";

        if (commandSplited.length == 1) {
            response.statusCode = 400;
            response.message = "Введите ключ: ";
        }
        else {
            key = commandSplited[1];
        }

        if (commandFields.getRequest().element != null){
            key = commandFields.getRequest().element.toString();
        }

        if (!key.isEmpty()){

            if (commandFields.getDatabase().getLabWorkDAO().getAll().containsKey(key)){

                String userName = TokenGenerator.decodeToken(commandFields.getRequest().authorization).userName;
                LabWork labWork = commandFields.getDatabase().getLabWorkDAO().get(key);
                if (userName.equals(labWork.getUserName())){
                    commandFields.getDatabase().getLabWorkDAO().delete(key);
                    commandFields.getDatabase().getLabWorkDAO().setLabWorksFromDatabase();
                    response.argument = "Элмент удалён";
                    response.contentType = Response.Type.TEXT;
                    response.statusCode = 200;
                }
                else {
                    response.message = "Вы можете удалять только свои элементы";
                    response.contentType = Response.Type.TEXT;
                    response.statusCode = 400;
                }

            }
            else {
                response.argument = "Элмент с таким ключом не найден";
                response.message = "Введите ключ: ";
                response.statusCode = 400;
            }

        }


        return response;

    }
}