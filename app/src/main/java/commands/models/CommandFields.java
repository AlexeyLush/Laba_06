package commands.models;

import commands.CommandsManager;
import dao.LabWorkDAO;
import exception.ParserException;
import files.DataFileManager;
import files.ExecuteFileManager;
import io.ConsoleManager;
import org.checkerframework.checker.units.qual.C;
import services.parsers.ParserJSON;

import javax.xml.crypto.Data;
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
    private List<String> listExecuteFiles;

    public CommandFields(Scanner scanner, String command, CommandsManager commandsManager,
                         ConsoleManager consoleManager) {
        this.scanner = scanner;
        this.command = command;
        this.commandsManager = commandsManager;
        this.consoleManager = consoleManager;
        this.listExecuteFiles = new ArrayList<>();
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

    public List<String> getListExecuteFiles() {
        return listExecuteFiles;
    }

    public void addExecutedFile(String fileName){
        this.listExecuteFiles.add(fileName);
    }

    public void removeExecutedFile(String fileName){
        this.listExecuteFiles.remove(fileName);
    }

    public void setListExecuteFiles(List<String> listExecuteFiles){
        this.listExecuteFiles = listExecuteFiles;
    }

    public CommandFields clone(String command, List<String> listExecuteFiles) throws CloneNotSupportedException {
        CommandFields commandFields = new CommandFields(this.scanner, command, this.commandsManager, this.consoleManager);
        commandFields.listExecuteFiles = listExecuteFiles;
        return commandFields;
    }
}
