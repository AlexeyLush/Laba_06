package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.LabWork;

import java.util.Map;

/**
 * Команда вывова элементов, значение поля description которых больше заданного
 */

public class FilterGreaterThanDescriptionCommand extends CommandAbstract {

    public FilterGreaterThanDescriptionCommand() {
        setTitle("filter_greater_than_description");
        setDescription("filter_greater_than_description description : вывести элементы, значение поля description которых больше заданного");
    }

    @Override
    public void execute(CommandFields commandFields) {

//        String[] commandSplited = commandFields.getCommand().split(" ");
//        String description;
//        if (commandSplited.length == 1) {
//            commandFields.getConsoleManager().output("Введите описание: ");
//            description = commandFields.getScanner().nextLine();
//            while ((description.isEmpty() || description.replaceAll(" ", "").replaceAll("\t", "").length() == 0)) {
//                commandFields.getConsoleManager().error("Описание не может быть пустым");
//                commandFields.getConsoleManager().output("Введите описание: ");
//                description = commandFields.getScanner().nextLine();
//            }
//        } else {
//            commandSplited[0] = "";
//            description = String.join(" ", commandSplited);
//        }
//
//        try {
//            if (commandFields.getLabWorkDAO().getAll().size() > 0){
//                commandFields.getConsoleManager().warning("----------------------------------------------");
//            }
//            for (Map.Entry<String, LabWork> entry : commandFields.getLabWorkDAO().getAll().entrySet()) {
//                commandFields.getConsoleManager().outputln(String.format("Ключ: %s", entry.getKey()));
//                commandFields.getConsoleManager().outputln(entry.getValue().toString());
//                commandFields.getConsoleManager().warning("----------------------------------------------");
//            }
//            commandFields.getConsoleManager().successfully("Команда filter_greater_than успешно выполнена");
//        } catch (NullPointerException nullPointerException) {
//            commandFields.getConsoleManager().error("Ошибка!");
//        }

    }
}
