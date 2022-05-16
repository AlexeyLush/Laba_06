package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.LabWork;
import response.Response;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Команда вывода суммы всех значений поля minimalPoint для всех элементов коллекции
 */

public class SumOfMinimalPointCommand extends CommandAbstract {

    public SumOfMinimalPointCommand(){
        setTitle("sum_of_minimal_point");
        setDescription("sum_of_minimal_point : вывести сумму значений поля minimalPoint для всех элементов коллекции");
    }

    @Override
    public Response execute(CommandFields commandFields) {

        Response response = new Response();
        response.status = Response.Status.OK;
        response.type = Response.Type.TEXT;
        response.command = "sum_of_minimal_point";

        try{

            double sum = 0;

            sum = commandFields
                    .getLabWorkDAO()
                    .getAll()
                    .values()
                    .stream()
                    .mapToDouble(LabWork::getMinimalPoint)
                    .sum();
            response.argument = String.format("Сумма минимальных точек: %f\n", sum);
        } catch (NullPointerException nullPointerException){
            response.status = Response.Status.ERROR;
            response.message = "Ошибка при выполнение команды!";
        }
        return response;
    }
}
