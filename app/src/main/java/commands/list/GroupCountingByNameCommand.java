package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.LabWork;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Команда группировки элементов коллекции по значению поля name, вывод количества элементов в каждой группе
 */

public class GroupCountingByNameCommand extends CommandAbstract {

    public GroupCountingByNameCommand(){
        setTitle("group_counting_by_name");
        setDescription("group_counting_by_name : сгруппировать элементы коллекции по значению поля name, вывести количество элементов в каждой группе");
    }

    @Override
    public void execute(CommandFields commandFields) {
        Map<Character, Integer> groupByName = new LinkedHashMap<>();
        try{
            for (Map.Entry<String, LabWork> entry : commandFields.getLabWorkDAO().getAll().entrySet()) {
                Character firstChar = entry.getValue().getName().toCharArray()[0];
                if (!groupByName.containsKey(firstChar)){
                    groupByName.put(firstChar, 0);
                }
                groupByName.put(firstChar, groupByName.get(firstChar) + 1);

            }

            for (Map.Entry<Character, Integer> entry : groupByName.entrySet()) {
                commandFields.getConsoleManager().outputln(String.format("Кол-во названий, начинающихся с '%c': %d",entry.getKey(), entry.getValue()));
            }

            commandFields.getConsoleManager().successfully("Команда group_counting_by_name успешно выполнена");
        } catch (NullPointerException nullPointerException){
            commandFields.getConsoleManager().error("Ошибка!");
        }
    }
}