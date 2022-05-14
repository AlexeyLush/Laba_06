package commands.interactive;

import io.ConsoleManager;
import models.Coordinates;
import models.LabWork;
import models.Person;
import request.Request;
import response.Response;
import services.checkers.LabWorkChecker;
import services.elementProcces.LabWorkProcess;
import services.parsers.ParserJSON;
import services.spliters.SplitCommandOnIdAndJSON;

import java.io.Reader;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

public class UpdateInteractiveCommand {

    private int counterFiled = 1;

    private void outputFiled(String field, ConsoleManager consoleManager, boolean isUpdateField) {

        if (isUpdateField) {
            consoleManager.outputln(String.format("%d. %s", counterFiled, field));
            counterFiled++;
        } else {
            consoleManager.outputln(String.format("%s", field));
        }
    }

    private void showLabWorkFields(LabWork labWork, ConsoleManager consoleManager) {
        consoleManager.warning("--------------------------------------------------------");
        outputFiled(String.format("ID: %s", labWork.getId()), consoleManager, false);
        outputFiled(String.format("Дата создания: %s", labWork.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))), consoleManager, false);
        outputFiled(String.format("Название работы: %s", labWork.getName()), consoleManager, true);
        outputFiled(String.format("Координата X: %d", labWork.getCoordinates().getX()), consoleManager, true);
        outputFiled(String.format("Координата Y: %d", labWork.getCoordinates().getY()), consoleManager, true);
        outputFiled(String.format("Минимальная точка: %f", labWork.getMinimalPoint()), consoleManager, true);
        outputFiled(String.format("Описание: %s", labWork.getDescription()), consoleManager, true);
        outputFiled(String.format("Сложность: %s", labWork.getDifficulty().getValue()), consoleManager, true);
        outputFiled(String.format("Имя автора: %s", labWork.getAuthor().getName()), consoleManager, true);
        outputFiled(String.format("Вес: %s", labWork.getAuthor().getWeight()), consoleManager, true);
        outputFiled(String.format("ID паспорта: %s", labWork.getAuthor().getPassportID()), consoleManager, true);
        consoleManager.warning("--------------------------------------------------------");
        counterFiled = 1;
    }

    private boolean choosePunct(ConsoleManager consoleManager, LabWorkChecker checker, Scanner scanner, LabWork labWork) {
        boolean isUpdate = true;
        LabWorkProcess labWorkProcess = new LabWorkProcess(consoleManager, scanner);
        try {
            int punctInt = Integer.parseInt(scanner.nextLine());
            switch (punctInt) {
                case 0 -> {
                    isUpdate = false;
                    break;
                }
                case 1 -> {
                    labWork.setName(null);
                    labWorkProcess.nameProcess(labWork, checker, false);
                    break;
                }
                case 2 -> {
                    Coordinates coordinates = labWork.getCoordinates();
                    coordinates.setX(null);
                    labWork.setCoordinates(coordinates);
                    labWorkProcess.coordinateXProcess(labWork, checker, false);
                    break;
                }
                case 3 -> {
                    Coordinates coordinates = labWork.getCoordinates();
                    coordinates.setY(null);
                    labWork.setCoordinates(coordinates);
                    labWorkProcess.coordinateYProcess(labWork, checker, false);
                    break;
                }
                case 4 -> {
                    labWork.setMinimalPoint(null);
                    labWorkProcess.minimalPointProcess(labWork, checker, false);
                    break;
                }
                case 5 -> {
                    labWork.setDescription(null);
                    labWorkProcess.descriptionProcess(labWork, checker, false);
                    break;
                }
                case 6 -> {
                    labWork.setDifficulty(null);
                    labWorkProcess.difficultyProcess(labWork, checker, false);
                    break;
                }
                case 7 -> {
                    Person person = labWork.getAuthor();
                    person.setName(null);
                    labWork.setAuthor(person);
                    labWorkProcess.personNameProcess(labWork, checker, false);
                    break;
                }
                case 8 -> {
                    Person person = labWork.getAuthor();
                    person.setWeight(null);
                    labWork.setAuthor(person);
                    labWorkProcess.personWeightProcess(labWork, checker, false);
                    break;
                }
                case 9 -> {
                    Person person = labWork.getAuthor();
                    person.setPassportID(null);
                    labWork.setAuthor(person);
                    labWorkProcess.personPassportIdProcess(labWork, checker, false);
                    break;
                }
                default -> {
                    consoleManager.error("Введите число от 1 до 9");
                }

            }

        } catch (NumberFormatException numberFormatException) {
            consoleManager.error("Введите число от 1 до 9");
        }
        return isUpdate;
    }

    public Request inputData(ConsoleManager consoleManager, Scanner scanner, Response response) {

        LabWorkChecker checker = new LabWorkChecker();

        Map.Entry<String, LabWork> entry = new ParserJSON().deserializeEntryLabWork(response.argument.toString());


        if (entry.getValue().getId() == 0) {

            consoleManager.error(response.message);

            LabWork labWork = new LabWork();

            consoleManager.output("Введите id: ");
            String id = scanner.nextLine();
            while (checker.checkId(id) == null) {
                consoleManager.error("Введите число больше нуля");
                consoleManager.output("Введите id: ");
                id = scanner.nextLine();
            }
            labWork.setId(Integer.parseInt(id));
            entry.setValue(labWork);
            return new Request("update", id);
        }

        try {
            if (response.status == Response.Status.OK) {
                showLabWorkFields(entry.getValue(), consoleManager);
                consoleManager.output("Выберете пункт, который хотите изменить или введите 0, чтобы завершить обновление: ");
                while (choosePunct(consoleManager, checker, scanner, entry.getValue())) {
                    showLabWorkFields(entry.getValue(), consoleManager);
                    consoleManager.output("Выберете пункт, который хотите изменить или введите 0, чтобы завершить обновление: ");
                }
            } else {
                consoleManager.error(response.message);
            }
        } catch (NullPointerException nullPointerException) {
            consoleManager.error("Вы не ввели значение");
        } catch (NumberFormatException numberFormatException) {
            consoleManager.error("Введите число");
        }

        return new Request("update", new ParserJSON().serializeElement(entry));

    }
}
