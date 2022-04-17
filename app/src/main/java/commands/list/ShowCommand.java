package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.LabWork;

import java.util.Map;

/**
 * Команда вывода в стандарный поток всех элементов коллекции в строковом представлении
 */

public class ShowCommand extends CommandAbstract {

    public ShowCommand(){
        setTitle("show");
        setDescription("show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
    }

    @Override
    public void execute(CommandFields commandFields) {

        try{
            if (commandFields.getLabWorkDAO().getAll().size() > 0){
                commandFields.getConsoleManager().warning("----------------------------------------------");
            }
            for (Map.Entry<String, LabWork> entry : commandFields.getLabWorkDAO().getAll().entrySet()) {
                commandFields.getConsoleManager().outputln(String.format("Ключ: %s", entry.getKey()));
                commandFields.getConsoleManager().outputln(entry.getValue().toString());
                commandFields.getConsoleManager().warning("----------------------------------------------");
            }
        }
        catch (NullPointerException nullPointerException){
            commandFields.getConsoleManager().error("Ошибка!");
        }
    }

}
