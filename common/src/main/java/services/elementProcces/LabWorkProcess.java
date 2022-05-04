package services.elementProcces;

import io.ConsoleManager;
import models.Coordinates;
import models.Difficulty;
import models.LabWork;
import models.Person;
import services.checkers.LabWorkChecker;

import java.util.Scanner;

public class LabWorkProcess implements ElementProcess<LabWork, LabWorkChecker> {

    private final ConsoleManager consoleManager;
    private final Scanner scanner;

    public LabWorkProcess(ConsoleManager consoleManager, Scanner scanner) {
        this.consoleManager = consoleManager;
        this.scanner = scanner;
    }


    public void nameProcess(LabWork labWork, LabWorkChecker checker, boolean withError) {

        String tempName = labWork.getName();

        if (tempName == null) {
            if (withError){
                consoleManager.error("Некорректное название лабораторной работы");
            }
            while (tempName == null) {
                consoleManager.output("Введите название лабораторной работы: ");
                tempName = checker.checkUserNameLab(scanner.nextLine());
            }
        }
        labWork.setName(tempName);
    }

    public void coordinateProcess(LabWork labWork) {
        Coordinates tempCoordinates = labWork.getCoordinates();

        if (tempCoordinates == null) {
            tempCoordinates = new Coordinates();
        }

        labWork.setCoordinates(tempCoordinates);

    }
    public void coordinateXProcess(LabWork labWork, LabWorkChecker checker, boolean withError){
        Long tempX = labWork.getCoordinates().getX();
        if (tempX == null) {
            if (withError){
                consoleManager.error("Некорректное поле X");
            }

            while (tempX == null) {
                consoleManager.output("Введите координату X: ");
                tempX = checker.checkX(scanner.nextLine());
            }
        }
        Coordinates coordinates = labWork.getCoordinates();
        coordinates.setX(tempX);
        labWork.setCoordinates(coordinates);
    }
    public void coordinateYProcess(LabWork labWork, LabWorkChecker checker, boolean withError){
        Integer tempY = labWork.getCoordinates().getY();
        if (tempY == null) {
            if (withError){
                consoleManager.error("Некорректное поле Y");
            }

            while (tempY == null) {
                consoleManager.output("Введите координату Y: ");
                tempY = checker.checkY(scanner.nextLine());
            }
        }

        Coordinates coordinates = labWork.getCoordinates();
        coordinates.setY(tempY);
        labWork.setCoordinates(coordinates);
    }

    public void minimalPointProcess(LabWork labWork, LabWorkChecker checker, boolean withError) {
        Float tempMinimalFloat = labWork.getMinimalPoint();
        if (tempMinimalFloat == null) {

            if (withError){
                consoleManager.error("Некорректная минимальная точка");
            }

            while (tempMinimalFloat == null) {
                consoleManager.output("Введите минимальную точку: ");
                tempMinimalFloat = checker.checkMinimalPoint(scanner.nextLine());
            }
        }
        labWork.setMinimalPoint(tempMinimalFloat);
    }
    public void descriptionProcess(LabWork labWork, LabWorkChecker checker, boolean withError) {
        String tempDescription = labWork.getDescription();
        if (tempDescription == null) {

            if (withError){
                consoleManager.error("Некорректное описание лабораторной работы");
            }

            while (tempDescription == null) {
                consoleManager.output("Введите описание лабораторной работы: ");
                tempDescription = checker.checkDescription(scanner.nextLine());
            }
        }
        labWork.setDescription(tempDescription);
    }
    public void difficultyProcess(LabWork labWork, LabWorkChecker checker, boolean withError) {

        Difficulty tempDifficulty = labWork.getDifficulty();
        if (tempDifficulty == null) {
            if (withError){
                consoleManager.error("Некорректная сложность лабороторной работы");
            }

            while (tempDifficulty == null) {
                Difficulty[] difficulties = Difficulty.values();
                for (int i = 0; i < difficulties.length; i++) {
                    consoleManager.warning(String.format("%s", difficulties[i]));
                }
                consoleManager.output("Введите сложность работы: ");
                tempDifficulty = checker.checkDifficulty(scanner.nextLine());
            }
        }
        labWork.setDifficulty(tempDifficulty);
    }

    public void personProcess(LabWork labWork) {
        Person tempAuthor = labWork.getAuthor();

        if (tempAuthor == null) {
            tempAuthor = new Person();
        }

        labWork.setAuthor(tempAuthor);

    }
    public void personNameProcess(LabWork labWork, LabWorkChecker checker, boolean withError){
        String tempAuthorName = labWork.getAuthor().getName();
        if (tempAuthorName == null) {

            if (withError){
                consoleManager.error("Некорректное имя автора");
            }

            while (tempAuthorName == null) {
                consoleManager.output("Введите имя автора: ");
                tempAuthorName = checker.checkNamePerson(scanner.nextLine());
            }
        }

        Person person = labWork.getAuthor();
        person.setName(tempAuthorName);
        labWork.setAuthor(person);
    }
    public void personWeightProcess(LabWork labWork, LabWorkChecker checker, boolean withError){

        Long tempAuthorWeight = labWork.getAuthor().getWeight();
        if (tempAuthorWeight == null) {

            if (withError){
                consoleManager.error("Некорректный вес");
            }

            while (tempAuthorWeight == null) {
                consoleManager.output("Введите вес: ");
                tempAuthorWeight = checker.checkWeightPerson(scanner.nextLine());
            }
        }

        Person person = labWork.getAuthor();
        person.setWeight(tempAuthorWeight);
        labWork.setAuthor(person);
    }
    public void personPassportIdProcess(LabWork labWork, LabWorkChecker checker, boolean withError){

        String tempAuthorPassportId = labWork.getAuthor().getPassportID();

        if (tempAuthorPassportId == null) {
            if (withError){
                consoleManager.error("Некорректный id паспорта");
            }

            while (tempAuthorPassportId == null) {
                consoleManager.output("Введите id паспорта: ");
                tempAuthorPassportId = checker.checkPassportIdPerson(scanner.nextLine());
            }
        }

        Person person = labWork.getAuthor();
        person.setPassportID(tempAuthorPassportId);
        labWork.setAuthor(person);
    }

    private LabWork processElement(LabWork labWork, LabWorkChecker checker, boolean withError, boolean withMessage){
        nameProcess(labWork, checker, withError);

        coordinateProcess(labWork);
        coordinateXProcess(labWork, checker,withError);
        coordinateYProcess(labWork, checker,withError);

        minimalPointProcess(labWork, checker, withError);
        descriptionProcess(labWork, checker, withError);
        difficultyProcess(labWork, checker, withError);

        personProcess(labWork);
        personNameProcess(labWork, checker, withError);
        personWeightProcess(labWork, checker, withError);
        personPassportIdProcess(labWork, checker, withError);

        return labWork;
    }

    @Override
    public LabWork getProcessedElementWithError(LabWork labWork, LabWorkChecker checker) {
        return processElement(labWork, checker, true, true);
    }

    @Override
    public LabWork getProcessedElement(LabWork labWork, LabWorkChecker checker) {
        return processElement(labWork, checker, false, true);
    }

}
