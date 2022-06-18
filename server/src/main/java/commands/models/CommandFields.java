package commands.models;

import commands.CommandsManager;
import dao.map.LabWorkDAO;
import database.Database;
import database.PostgresDatabase;
import io.ConsoleManager;
import request.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Класс для моделей
 */

public class CommandFields {

    private final Scanner scanner;
    private final String command;
    private final CommandsManager commandsManager;
    private final ConsoleManager consoleManager;
    private final Request request;
    private final PostgresDatabase database;
    private List<String> listExecuteFiles;

    public CommandFields(Scanner scanner, String command, CommandsManager commandsManager,
                         ConsoleManager consoleManager, Request request, PostgresDatabase database) {
        this.scanner = scanner;
        this.command = command;
        this.commandsManager = commandsManager;
        this.consoleManager = consoleManager;
        this.listExecuteFiles = new ArrayList<>();
        this.request = request;
        this.database = database;
    }

    public PostgresDatabase getDatabase() {
        return database;
    }

    public Scanner getScanner(){
        return scanner;
    }

    public String getCommand() {
        return command;
    }


    public CommandsManager getCommandsManager() {
        return commandsManager;
    }

    public ConsoleManager getConsoleManager() {
        return consoleManager;
    }

    public Request getRequest() {
        return this.request;
    }
}
