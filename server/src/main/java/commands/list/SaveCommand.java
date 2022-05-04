package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import response.Response;

/**
 * Команда сохранения коллекции в файл
 */

public class SaveCommand extends CommandAbstract {

    public SaveCommand(){
        setTitle("save");
        setDescription("save : сохранить коллекцию в файл");
    }

    @Override
    public Response execute(CommandFields commandFields) {
//        commandFields.getDataFileManager().save(commandFields.getLabWorkDAO().getAll());
        return null;
    }
}
