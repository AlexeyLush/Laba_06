package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import response.Response;

/**
 * Команда удаления элементов из коллекции по его ключу
 */

public class RemoveKeyCommand extends CommandAbstract {

    public RemoveKeyCommand(){
        setTitle("remove_key");
        setDescription("remove_key null : удалить элемент из коллекции по его ключу");
    }

    @Override
    public Response execute(CommandFields commandFields) {
//        String[] commandSplited = commandFields.getCommand().split(" ");
//        String key = "";
//        if (commandSplited.length == 1){
//            while (true){
//                try{
//                    commandFields.getConsoleManager().output("Введите ключ: ");
//                    key = commandFields.getScanner().nextLine();
//                    if ((key.isEmpty() || key.replaceAll(" ", "").replaceAll("\t", "").length() == 0)){
//                        throw new NotFoundKeyException();
//                    }
//                    if (!commandFields.getLabWorkDAO().getAll().containsKey(key)){
//                        commandFields.getConsoleManager().error("Элемент с ключом не найден!");
//                    } else{
//                        commandFields.getLabWorkDAO().delete(key);
//                        break;
//                    }
//                } catch (NullPointerException nullPointerException){
//                    commandFields.getConsoleManager().error("Ошибка!");
//                } catch (NotFoundKeyException notFoundKeyException){
//                    notFoundKeyException.outputException();
//                }
//            }
//        } else{
//            key = commandSplited[1];
//            if (!commandFields.getLabWorkDAO().getAll().containsKey(key)){
//                commandFields.getConsoleManager().error("Элемент с ключом не найден!");
//            } else{
//                commandFields.getLabWorkDAO().delete(key);
//                commandFields.getConsoleManager().successfully("Команда remove_key успешно выполнена");
//            }
//        }

        return null;
    }
}