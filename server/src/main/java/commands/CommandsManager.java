package commands;

import commands.list.*;
import commands.models.CommandFields;
import dao.map.LabWorkDAO;
import database.PostgresDatabase;
import io.ConsoleManager;
import request.Request;
import response.Response;
import server.Server;

import java.util.*;

/**
 * Класс менеджер команд
 */

public final class CommandsManager {

    private final Map<String, CommandAbstract> commandsList = new LinkedHashMap<String, CommandAbstract>();
    private Scanner scanner;
    private PostgresDatabase postgresDatabase;
    private final ConsoleManager consoleManager;

    public CommandsManager(Scanner scanner, ConsoleManager consoleManager, PostgresDatabase postgresDatabase){
        this.scanner = scanner;
        this.consoleManager = consoleManager;
        this.postgresDatabase = postgresDatabase;

        addCommand(new HelpCommand());
        addCommand(new InfoCommand());
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

    public Response executeCommand(Request request){
        String commandName = request.message.split(" ")[0].toLowerCase();
        try {
            if (commandsList.containsKey(commandName)) {
                CommandFields commandFields = new CommandFields(scanner, request.message,this, consoleManager, request, this.postgresDatabase);
                return commandsList.get(commandName).execute(commandFields);
            }
            else{
                return new Response(404, Response.Type.TEXT, "", "Команда не найдена", "get");
            }
        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e){
            return new Response(404, Response.Type.TEXT, "", "Команда не найдена", "get");
        }
    }

    public Response inputCommand(Request request) {
        try{
            return executeCommand(request);
        } catch (NoSuchElementException e){
            consoleManager.warning("Принудительный выход...");
            Server.exit();
        }
        return new Response(500, Response.Type.TEXT, "", "Ошибка на сервере", "get");
    }

}
