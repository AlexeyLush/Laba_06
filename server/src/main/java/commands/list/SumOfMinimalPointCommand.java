package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.LabWork;
import response.Response;


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
        response.statusCode = 200;
        response.contentType = Response.Type.TEXT;
        response.command = "sum_of_minimal_point";

        try{

            double sum = 0;

            sum = commandFields
                    .getDatabase().getLabWorkDAO()
                    .getAll()
                    .values()
                    .stream()
                    .mapToDouble(LabWork::getMinimalPoint)
                    .sum();
            response.argument = String.format("Сумма минимальных точек: %f\n", sum);
        } catch (NullPointerException nullPointerException){
            response.statusCode = 500;
            response.message = "Ошибка при выполнение команды!";
        }
        return response;
    }
}
