package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.LabWork;
import services.parsers.ParserJSON;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Команда вывода в стандартный поток информации о коллекции (тип, дата инициализации, количество элементов и т.д.
 */

public class InfoCommand extends CommandAbstract {

    public InfoCommand(){
        setTitle("info");
        setDescription("info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
    }

    @Override
    public void execute(CommandFields commandFields) {
//        String date = new ParserJSON(commandFields.getConsoleManager()).getDataFromFile(commandFields.getDataFileManager().readFile());
//
//        commandFields.getConsoleManager().warning("-------------------------------------------------------------");
//        commandFields.getConsoleManager().outputln(String.format("Время инициализации: %s", ZonedDateTime.parse(date).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))));
//        commandFields.getConsoleManager().outputln(String.format("Тип коллекции: %s", commandFields.getLabWorkDAO().getAll().getClass()));
//        commandFields.getConsoleManager().outputln(String.format("Кол-во элментов: %s", commandFields.getLabWorkDAO().getAll().size()));
//        commandFields.getConsoleManager().warning("-------------------------------------------------------------");
    }
}
