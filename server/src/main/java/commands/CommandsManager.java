package commands;

import commands.list.*;
import commands.models.CommandFields;
import dao.LabWorkDAO;
import files.DataFileManager;
import io.ConsoleManager;
import models.LabWork;
import response.Response;
import server.Server;

import java.util.*;

/**
 * Класс менеджер команд
 */

public final class CommandsManager {

    private final Map<String, CommandAbstract> commandsList = new LinkedHashMap<String, CommandAbstract>();
    private Scanner scanner;
    private final ConsoleManager consoleManager;
    private LabWorkDAO labWorkDAO;
    private DataFileManager dataFileManager;

    public CommandsManager(Scanner scanner, ConsoleManager consoleManager, LabWorkDAO labWorkDAO, DataFileManager dataFileManager){
        this.scanner = scanner;
        this.consoleManager = consoleManager;
        this.labWorkDAO = labWorkDAO;
        this.dataFileManager = dataFileManager;

        addCommand(new HelpCommand());
        addCommand(new InfoCommand());
        addCommand(new ClearCommand());
        addCommand(new SaveCommand());
        addCommand(new ExecuteScriptCommand());
        addCommand(new RemoveGreaterCommand());
        addCommand(new RemoveLowerCommand());
        addCommand(new RemoveKeyCommand());
        addCommand(new InsertCommand());
        addCommand(new ShowCommand());
        addCommand(new UpdateCommand());
        addCommand(new ReplaceIfLowerCommand());
        addCommand(new SumOfMinimalPointCommand());
        addCommand(new GroupCountingByNameCommand());
        addCommand(new FilterGreaterThanDescriptionCommand());
    }

    private void addCommand(CommandAbstract command){
        commandsList.put(command.getTitle(), command);
    }

    public Map<String, CommandAbstract> getCommandsList(){
        return new LinkedHashMap<>(this.commandsList);
    }

    public Response executeCommand(String command){
        String commandName = command.split(" ")[0].toLowerCase();
        try {
            if (commandsList.containsKey(commandName)) {
                CommandFields commandFields = new CommandFields(scanner, command, labWorkDAO,
                        this, dataFileManager, consoleManager);
                return commandsList.get(commandName).execute(commandFields);
            }
            else{
                return new Response(Response.Status.ERROR, Response.Type.TEXT, "Команда не найдена\n");
            }
        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e){
            return new Response(Response.Status.ERROR,Response.Type.TEXT, "Команда не найдена\n");
        }
    }

    public void executeCommand(String command, LabWorkDAO labWorkDAO, List<String> listExecutedFiles){
        String commandName = command.split(" ")[0].toLowerCase();
        try{
            if (commandsList.containsKey(commandName)){
                CommandFields commandFields = new CommandFields(scanner, command, labWorkDAO,
                        this, dataFileManager, consoleManager);
                commandFields.setListExecuteFiles(listExecutedFiles);
                commandsList.get(commandName).execute(commandFields);
            }
        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e){
            consoleManager.error("Команда не найдена\n");
            scanner = new Scanner(System.in);
        }
    }


    public Response inputCommand(String command) {
        try{
            return executeCommand(command);
        } catch (NoSuchElementException e){
            consoleManager.warning("Принудительный выход...");
            Server.exit();
        }
        return new Response(Response.Status.ERROR,Response.Type.TEXT, "Ошибка на сервере\n");
    }

}
