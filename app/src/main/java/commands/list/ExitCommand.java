package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import laba.App;

/**
 * Команда завершения программы (без сохранения в файл)
 */

public class ExitCommand extends CommandAbstract {

    public ExitCommand(){
        setTitle("exit");
        setDescription("exit : завершить программу (без сохранения в файл)");
    }

    @Override
    public void execute(CommandFields commandFields) {
        App.exit();
    }
}
