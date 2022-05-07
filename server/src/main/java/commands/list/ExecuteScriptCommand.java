package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import files.ExecuteFileManager;
import request.Request;
import response.Response;

import java.io.File;
import java.util.List;

/**
 * Команда считывания и исполнения скрипта из указанного файла
 */

public class ExecuteScriptCommand extends CommandAbstract {

    public ExecuteScriptCommand() {
        setTitle("execute_script");
        setDescription("execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
    }


    public void executeScriptsInFile(List<String> scripts, List<String> listExecuteFiles, CommandFields commandFields) throws CloneNotSupportedException {
        for (String command : scripts) {
            CommandFields newCommandField = commandFields.clone(command, listExecuteFiles);
            commandFields.getCommandsManager().executeCommand(commandFields.getRequest(), commandFields.getLabWorkDAO(), listExecuteFiles);
        }
    }

    @Override
    public Response execute(CommandFields commandFields) {

        Response response = new Response();

        String[] commandSplited = commandFields.getRequest().commandName.split(" ");
        String fileName = "";
        Request request = commandFields.getRequest();
        if (commandSplited.length == 1) {
            response.status = Response.Status.ERROR;
            response.type = Response.Type.INPUT;
            if (request.element != null){
                response.status = Response.Status.OK;
                response.type = Response.Type.LIST;
                response.argument = request.element;
            }
        } else {

            fileName = commandSplited[1];

            response.status = Response.Status.OK;
            response.type = Response.Type.LIST;
            response.argument = fileName;
        }

        return response;

    }
}
