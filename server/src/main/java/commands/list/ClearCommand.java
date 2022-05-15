package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import models.Coordinates;
import models.Difficulty;
import models.LabWork;
import models.Person;
import models.service.GenerationID;
import response.Response;

/**
 * Команда очистки коллекции
 */

public class ClearCommand extends CommandAbstract {

    public ClearCommand(){
        setTitle("clear");
        setDescription("clear: очистить коллекцию");
    }

    @Override
    public Response execute(CommandFields commandFields) {
        commandFields.getLabWorkDAO().clear();
//        for (int i = 0; i < 3000; i++){
//            LabWork labWork = new LabWork();
//            labWork.setId(GenerationID.newId());
//
//            labWork.setName("Лабораторная работа №1");
//
//            Coordinates coordinates = new Coordinates();
//            coordinates.setX(5L);
//            coordinates.setY(3);
//
//            labWork.setCoordinates(coordinates);
//            labWork.setMinimalPoint(5f);
//            labWork.setDescription("Это описание лабораторной работы №1");
//
//            labWork.setDifficulty(Difficulty.NORMAL);
//
//            Person author = new Person();
//            author.setName("Alexey");
//            author.setWeight(26L);
//            author.setPassportID("336803");
//
//            labWork.setAuthor(author);
//
//            commandFields.getLabWorkDAO().create(String.format("%d", i), labWork);
//        }
//        commandFields.getDataFileManager().save(commandFields.getLabWorkDAO().getAll());
        return new Response(Response.Status.OK, Response.Type.TEXT, "Коллекция очищена\n");
    }
}
