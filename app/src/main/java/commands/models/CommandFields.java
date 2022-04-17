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
    private final LabWorkDAO labWorkDAO;
    private final CommandsManager commandsManager;
    private final ConsoleManager consoleManager;
    private final DataFileManager dataFileManager;
    private List<String> listExecuteFiles;

    public CommandFields(Scanner scanner, String command, LabWorkDAO labWorkDAO, CommandsManager commandsManager, DataFileManager dataFileManager,
                         ConsoleManager consoleManager) {
        this.scanner = scanner;
        this.command = command;
        this.labWorkDAO = labWorkDAO;
        this.commandsManager = commandsManager;
        this.dataFileManager = dataFileManager;
        this.consoleManager = consoleManager;
        this.listExecuteFiles = new ArrayList<>();
    }

    public Scanner getScanner(){
        return scanner;
    }

    public String getCommand() {
        return command;
    }

    public LabWorkDAO getLabWorkDAO() {
        return labWorkDAO;
    }

    public DataFileManager getDataFileManager() {
        return dataFileManager;
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
        CommandFields commandFields = new CommandFields(this.scanner, command, this.getLabWorkDAO(), this.commandsManager, this.dataFileManager, this.consoleManager);
        commandFields.listExecuteFiles = listExecuteFiles;
        return commandFields;
    }
}
