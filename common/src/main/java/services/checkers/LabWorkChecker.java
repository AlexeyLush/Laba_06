package services.checkers;

import dao.LabWorkDAO;
import io.ConsoleManager;
import models.Coordinates;
import models.Difficulty;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

public class LabWorkChecker extends Checker {

    public String checkerKey(String key, LabWorkDAO labWorkDAO, ConsoleManager consoleManager, boolean withError) {
        String returnKey = null;
        try {
            if (labWorkDAO.getAll().containsKey(key)) {
                return null;
            }
            returnKey = key;
        } catch (NoSuchElementException noSuchElementException) {
            return null;
        }
        return returnKey;
    }
    public String checkUserKey(String key, LabWorkDAO labWorkDAO, ConsoleManager consoleManager, boolean isUnique, boolean withError) {
        String returnKey = null;
        if (key == null) {
            return null;
        } else if (key.isEmpty() || key.replaceAll(" ", "").replaceAll("\t", "").length() == 0 || key.contains(" ") || key.contains("\t")) {
            return null;
        } else {
            if (isUnique){
                returnKey = checkerKey(key, labWorkDAO, consoleManager, withError);
            } else {
                returnKey = key;
            }
        }
        return returnKey;
    }
    public Integer checkId(String id, ConsoleManager consoleManager, boolean withError){
        Integer returnId = null;

        try{
            if (id == null || id.isEmpty() || id.replaceAll(" ", "").replaceAll("\t", "").length() == 0){
                return null;
            }
            returnId = Integer.parseInt(id);
            if (returnId <= 0){
                return null;
            }
        } catch (NumberFormatException numberFormatException){
            return null;
        }
        return returnId;

    }
    public ZonedDateTime checkDate(String date, ConsoleManager consoleManager, boolean withError){
        ZonedDateTime returnDate;
        try {
            returnDate = ZonedDateTime.parse(date);
        }  catch (DateTimeException dateTimeException){
            return null;
        }
        return returnDate;
    }
    public String checkUserNameLab(String name, ConsoleManager consoleManager, boolean withError){

        String returnName = null;
        returnName = name;
        if (name == null || name.isEmpty() || name.replaceAll(" ", "").replaceAll("\t", "").length() == 0){
            return null;
        }
        return returnName;
    }
    public Long checkX(String x, ConsoleManager consoleManager, boolean withError){
        Long returnX = null;
        Coordinates tempCoordinates = new Coordinates();
        try {
            returnX = Long.parseLong(x);
            if (returnX > tempCoordinates.getMaxCoordinateX()){
                return null;
            }
        } catch (NumberFormatException numberFormatException){
            return null;
        }
        return returnX;
    }
    public Integer checkY(String y, ConsoleManager consoleManager, boolean withError){
        Integer returnY = null;
        try {
            returnY = Integer.parseInt(y);
        } catch (NumberFormatException numberFormatException){
            return null;
        }
        return returnY;
    }
    public Float checkMinimalPoint(String minimalPoint, ConsoleManager consoleManager, boolean withError){
        Float returnMinimalPoint = null;
        try {
            if (minimalPoint == null){
                return null;
            }
            returnMinimalPoint = Float.parseFloat(minimalPoint.replace(",", "."));
            if (returnMinimalPoint <= 0){
                return null;
            }
        }catch (NumberFormatException numberFormatException){
            return null;
        }
        return returnMinimalPoint;
    }
    public String checkDescription(String description, ConsoleManager consoleManager, boolean withError){
        String returnDescription;

            returnDescription = description;
            if (description == null || description.isEmpty() || description.replaceAll(" ", "").replaceAll("\t", "").length() == 0){
                return null;
            }

        return returnDescription;
    }
    public Difficulty checkDifficulty(String difficultyString, ConsoleManager consoleManager, boolean withError){
        Difficulty difficulty = Difficulty.isDifficulty(difficultyString);

        if (difficulty == null){
            return null;
        }

        return difficulty;
    }
    public String checkNamePerson(String name, ConsoleManager consoleManager, boolean withError){
        String returnName;
            returnName = name;
            if (name == null || name.isEmpty() || name.replaceAll(" ", "").replaceAll("\t", "").length() == 0){
                return null;
            }
        return returnName;
    }
    public Long checkWeightPerson(String weight, ConsoleManager consoleManager, boolean withError){

        Long returnWeight = null;
        try{
            if (weight == null){
                return null;
            }
            returnWeight = Long.parseLong(weight);
            if (returnWeight <= 0){
                return null;
            }
        } catch (NumberFormatException numberFormatException){
            return null;
        }
        return returnWeight;
    }
    public String checkPassportIdPerson(String passport, ConsoleManager consoleManager, boolean withError){
        String returnPassportId = null;
        returnPassportId = passport;
        if (returnPassportId == null || returnPassportId.isEmpty() || returnPassportId.replaceAll(" ", "").replaceAll("\t", "").length() == 0){
            return null;
        }
        return returnPassportId;
    }
}