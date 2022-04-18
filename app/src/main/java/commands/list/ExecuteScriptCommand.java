package commands.list;

import commands.CommandAbstract;

import commands.models.CommandFields;

import exception.NotFoundScriptFileException;
import files.ExecuteFileManager;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Команда считывания и исполнения скрипта из указанного файла
 */

public class ExecuteScriptCommand extends CommandAbstract {

    public ExecuteScriptCommand(){
        setTitle("execute_script");
        setDescription("execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
    }


    public void executeScriptsInFile(List<String> scripts, List<String> listExecuteFiles, CommandFields commandFields) throws CloneNotSupportedException {
        for (String command: scripts){
            CommandFields newCommandField = commandFields.clone(command, listExecuteFiles);
            commandFields.getCommandsManager().executeCommand(command, listExecuteFiles);
        }
    }

    @Override
    public void execute(CommandFields commandFields) {

        String[] commandSplited = commandFields.getCommand().split(" ");
        String fileName = "";
        if (commandSplited.length == 1){
            commandFields.getConsoleManager().output("Введите имя файла (файл должен находится в папке script): ");
            fileName = commandFields.getScanner().nextLine();
        }
        else{
            fileName = commandSplited[1];
        }

        File executeFile = new File(String.format("scripts/%s.txt", fileName));
        try {

            if (!executeFile.isFile()){
                throw new NotFoundScriptFileException();
            }

            if (!commandFields.getListExecuteFiles().contains(fileName)){
                commandFields.addExecutedFile(fileName);
                ExecuteFileManager executeFileManager = new ExecuteFileManager(executeFile.getAbsolutePath(), commandFields.getConsoleManager());
                List<String> scripts = executeFileManager.readFile();
                executeScriptsInFile(scripts, commandFields.getListExecuteFiles(), commandFields);
                commandFields.removeExecutedFile(fileName);

            } else{
                commandFields.getConsoleManager().warning(String.format("Файл %s уже был исполнен", fileName));
            }


        } catch (NotFoundScriptFileException e) {
            e.outputException();
        } catch (CloneNotSupportedException e) {
            commandFields.getConsoleManager().error("Во время выполнения программы произошла ошибка");
        }
    }
}
