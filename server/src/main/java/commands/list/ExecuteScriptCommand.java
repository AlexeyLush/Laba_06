package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import request.Request;
import response.Response;

/**
 * Команда считывания и исполнения скрипта из указанного файла
 */

public class ExecuteScriptCommand extends CommandAbstract {

    public ExecuteScriptCommand() {
        setTitle("execute_script");
        setDescription("execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
    }



    @Override
    public Response execute(CommandFields commandFields) {

        Response response = new Response();

        String[] commandSplited = commandFields.getRequest().message.toString().split(" ");
        String fileName = "";
        Request request = commandFields.getRequest();
        if (commandSplited.length == 1) {
            response.statusCode = 400;
            response.contentType = Response.Type.INPUT_SCRIPT;
            if (request.element != null){
                response.statusCode = 200;
                response.contentType = Response.Type.LIST;
                response.argument = request.element;
            }
        } else {
            fileName = commandSplited[1];
            response.statusCode = 200;
            response.contentType = Response.Type.LIST;
            response.argument = fileName;
        }

        return response;

    }
}
