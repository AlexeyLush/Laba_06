package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.LabWork;

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
    public void execute(CommandFields commandFields) {
        float sum = 0;
        try{
            for (Map.Entry<String, LabWork> entry : commandFields.getLabWorkDAO().getAll().entrySet()) {
                sum += entry.getValue().getMinimalPoint();
            }
        } catch (NullPointerException nullPointerException){
            commandFields.getConsoleManager().error("Ошибка при исполнение команды");
        }
        commandFields.getConsoleManager().outputln(String.format("Сумма всех minimalPoint: %f", sum));
    }
}
