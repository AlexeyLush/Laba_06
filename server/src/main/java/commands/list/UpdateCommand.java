package commands.list;

import commands.CommandAbstract;
import commands.models.CommandFields;
import io.ConsoleManager;
import models.Coordinates;
import models.LabWork;
import models.Person;
import response.Response;
import services.checkers.LabWorkChecker;
import services.elementProcces.LabWorkProcess;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Команда обновления значений элемента коллекции, id которого равен заданному
 */

public class UpdateCommand extends CommandAbstract {

    public UpdateCommand() {
        setTitle("update");
        setDescription("update id {element} : обновить значение элемента коллекции, id которого равен заданному");
    }

//    private boolean choosePunct(String punct, ConsoleManager consoleManager, LabWorkChecker checker, Scanner scanner, LabWork labWork) {
//        boolean isUpdate = true;
//        LabWorkProcess labWorkProcess = new LabWorkProcess(consoleManager, scanner);
//        try {
//            int punctInt = Integer.parseInt(punct);
//            switch (punctInt) {
//                case 0 : {
//                    isUpdate = false;
//                    break;
//                }
//                case 1 : {
//                    labWork.setName(null);
//                    labWorkProcess.nameProcess(labWork, checker, false);
//                    break;
//                }
//                case 2 : {
//                    Coordinates coordinates = labWork.getCoordinates();
//                    coordinates.setX(null);
//                    labWork.setCoordinates(coordinates);
//                    labWorkProcess.coordinateXProcess(labWork, checker, false);
//                    break;
//                }
//                case 3 : {
//                    Coordinates coordinates = labWork.getCoordinates();
//                    coordinates.setY(null);
//                    labWork.setCoordinates(coordinates);
//                    labWorkProcess.coordinateYProcess(labWork, checker, false);
//                    break;
//                }
//                case 4 : {
//                    labWork.setMinimalPoint(null);
//                    labWorkProcess.minimalPointProcess(labWork, checker, false);
//                    break;
//                }
//                case 5 : {
//                    labWork.setDescription(null);
//                    labWorkProcess.descriptionProcess(labWork, checker, false);
//                    break;
//                }
//                case 6 : {
//                    labWork.setDifficulty(null);
//                    labWorkProcess.difficultyProcess(labWork, checker, false);
//                    break;
//                }
//                case 7 : {
//                    Person person = labWork.getAuthor();
//                    person.setName(null);
//                    labWork.setAuthor(person);
//                    labWorkProcess.personNameProcess(labWork, checker, false);
//                    break;
//                }
//                case 8 : {
//                    Person person = labWork.getAuthor();
//                    person.setWeight(null);
//                    labWork.setAuthor(person);
//                    labWorkProcess.personWeightProcess(labWork, checker, false);
//                    break;
//                }
//                case 9 : {
//                    Person person = labWork.getAuthor();
//                    person.setPassportID(null);
//                    labWork.setAuthor(person);
//                    labWorkProcess.personPassportIdProcess(labWork, checker, false);
//                    break;
//                }
//                default : {
//                    consoleManager.error("Введите число от 1 до 9");
//                }
//            }
//        } catch (NumberFormatException numberFormatException) {
//            new NotNumberException().outputException();
//        }
//        return isUpdate;
//    }
//
//    private int counterFiled = 1;
//
//    private void outputFiled(String field, ConsoleManager consoleManager, boolean isUpdateField) {
//
//        if (isUpdateField) {
//            consoleManager.outputln(String.format("%d. %s", counterFiled, field));
//            counterFiled++;
//        } else {
//            consoleManager.outputln(String.format("%s", field));
//        }
//    }
//
//    private void showLabWorkFields(LabWork labWork, ConsoleManager consoleManager) {
//        outputFiled(String.format("ID: %s", labWork.getId()), consoleManager, false);
//        outputFiled(String.format("Дата создания: %s", labWork.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))), consoleManager, false);
//        outputFiled(String.format("Название работы: %s", labWork.getName()), consoleManager, true);
//        outputFiled(String.format("Координата X: %d", labWork.getCoordinates().getX()), consoleManager, true);
//        outputFiled(String.format("Координата Y: %d", labWork.getCoordinates().getY()), consoleManager, true);
//        outputFiled(String.format("Минимальная точка: %f", labWork.getMinimalPoint()), consoleManager, true);
//        outputFiled(String.format("Описание: %s", labWork.getDescription()), consoleManager, true);
//        outputFiled(String.format("Сложность: %s", labWork.getDifficulty().getValue()), consoleManager, true);
//        outputFiled(String.format("Имя автора: %s", labWork.getAuthor().getName()), consoleManager, true);
//        outputFiled(String.format("Вес: %s", labWork.getAuthor().getWeight()), consoleManager, true);
//        outputFiled(String.format("ID паспорта: %s", labWork.getAuthor().getPassportID()), consoleManager, true);
//        consoleManager.warning("--------------------------------------------------------");
//        counterFiled = 1;
//    }

    @Override
    public Response execute(CommandFields commandFields) {

//        LabWorkChecker checker = new LabWorkChecker();
//        LabWork labWork = null;
//
//        String[] splitCommand = new SplitCommandOnIdAndJSON().splitedCommand(commandFields.getCommand(), commandFields.getConsoleManager());
//
//        try {
//            String id = splitCommand[0];
//            String json = splitCommand[1];
//
//            Integer idInt = checker.checkId(id, commandFields.getConsoleManager(), true);
//            while (labWork == null){
//
//                while (idInt == null){
//                    commandFields.getConsoleManager().output("Введите id: ");
//                    idInt = checker.checkId(commandFields.getScanner().nextLine(), commandFields.getConsoleManager(), true);
//                }
//                for (Map.Entry<String, LabWork> entry : commandFields.getLabWorkDAO().getAll().entrySet()) {
//                    if (entry.getValue().getId().equals(idInt)) {
//                        labWork = entry.getValue();
//                    }
//                }
//
//                if (labWork == null) {
//                    idInt = null;
//                    commandFields.getConsoleManager().error("Элемент с таким id не найден");
//                }
//            }
//
//
//            if (json != null) {
//
//                LabWork labWorkTemp = new ParserJSON(commandFields.getConsoleManager()).deserializeElement(json);
//                LabWorkProcess labWorkJsonProcess = new LabWorkProcess(commandFields.getConsoleManager(), commandFields.getScanner());
//                LabWork labWorkProcessed = labWorkJsonProcess.getProcessedElementWithError(labWorkTemp, checker);
//                commandFields.getLabWorkDAO().update(idInt, labWorkProcessed);
//                commandFields.getConsoleManager().successfully("Команда update успешно выполнена");
//
//            }
//            else {
//                showLabWorkFields(labWork, commandFields.getConsoleManager());
//                commandFields.getConsoleManager().output("Выберете пункт, который хотите изменить или введите 0, чтобы завершить обновление: ");
//                while (choosePunct(commandFields.getScanner().nextLine(), commandFields.getConsoleManager(), checker, commandFields.getScanner(), labWork)) {
//                    showLabWorkFields(labWork, commandFields.getConsoleManager());
//                    commandFields.getConsoleManager().output("Выберете пункт, который хотите изменить или введите 0, чтобы завершить обновление: ");
//                }
//            }
//
//
//        } catch (NullPointerException nullPointerException) {
//            commandFields.getConsoleManager().error("Вы не ввели значение");
//        } catch (NumberFormatException numberFormatException) {
//            commandFields.getConsoleManager().error("Введите число");
//        }

        return null;

    }
}
