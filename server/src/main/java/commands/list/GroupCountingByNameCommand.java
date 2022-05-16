package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.LabWork;
import response.Response;

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
    public Response execute(CommandFields commandFields) {

        Response response = new Response();
        response.status = Response.Status.OK;
        response.type = Response.Type.TEXT;
        response.command = "group_counting_by_name";
        Map<Character, Integer> groupByName = new LinkedHashMap<>();
        try{

            commandFields
                    .getLabWorkDAO()
                    .getAll()
                    .forEach((key, value) -> {
                        Character firstChar = value.getName().toCharArray()[0];
                        if (!groupByName.containsKey(firstChar)) {
                            groupByName.put(firstChar, 0);
                        }
                        groupByName.put(firstChar, groupByName.get(firstChar) + 1);
                    });

            response.argument = "";
            groupByName
                    .entrySet()
                    .stream()
                    .forEach(entry -> {
                        response.argument += String.format("Кол-во названий, начинающихся с '%c': %d\n", entry.getKey(), entry.getValue());
                    });


        } catch (NullPointerException nullPointerException){
            response.status = Response.Status.ERROR;
            response.message = "Ошибка при выполнение команды!";
        }
        return response;
    }
}