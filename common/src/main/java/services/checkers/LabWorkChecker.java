package services.checkers;

import models.Coordinates;
import models.Difficulty;

import java.time.DateTimeException;
import java.time.ZonedDateTime;

public class LabWorkChecker extends Checker {

    public String checkUserKey(String key) {
        if (key == null) {
            return null;
        } else if (key.isEmpty() || key.replaceAll(" ", "").replaceAll("\t", "").length() == 0 || key.contains(" ") || key.contains("\t") || key.length() > 255) {
            return null;
        } else {
            return key;
        }
    }
    public Integer checkId(String id){
        Integer returnId = null;

        try{
            if (id == null || id.isEmpty() || id.replaceAll(" ", "").replaceAll("\t", "").length() == 0 || id.length() > 255){
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
    public ZonedDateTime checkDate(String date){
        ZonedDateTime returnDate;
        try {
            returnDate = ZonedDateTime.parse(date);
        }  catch (DateTimeException dateTimeException){
            return null;
        }
        return returnDate;
    }
    public String checkUserNameLab(String name){

        String returnName = null;
        returnName = name;
        if (name == null || name.isEmpty() || name.replaceAll(" ", "").replaceAll("\t", "").length() == 0 || name.length() > 255){
            return null;
        }
        return returnName;
    }
    public Long checkX(String x){
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
    public Integer checkY(String y){
        Integer returnY = null;
        try {
            returnY = Integer.parseInt(y);
        } catch (NumberFormatException numberFormatException){
            return null;
        }
        return returnY;
    }
    public Float checkMinimalPoint(String minimalPoint){
        Float returnMinimalPoint = null;
        try {
            if (minimalPoint == null){
                return null;
            }
            returnMinimalPoint = Float.parseFloat(minimalPoint.replace(",", "."));
            if (returnMinimalPoint <= 0 || returnMinimalPoint.isInfinite()){
                return null;
            }
        }catch (NumberFormatException numberFormatException){
            return null;
        }
        return returnMinimalPoint;
    }
    public String checkDescription(String description){
        String returnDescription;

            returnDescription = description;
            if (description == null || description.isEmpty() || description.replaceAll(" ", "").replaceAll("\t", "").length() == 0 || description.length() > 255){
                return null;
            }

        return returnDescription;
    }
    public Difficulty checkDifficulty(String difficultyString){
        Difficulty difficulty = Difficulty.isDifficulty(difficultyString);

        if (difficulty == null){
            return null;
        }

        return difficulty;
    }
    public String checkNamePerson(String name){
        String returnName;
        returnName = name;
        if (name == null || name.isEmpty() || name.replaceAll(" ", "").replaceAll("\t", "").length() == 0 || name.length() > 255){
            return null;
        }
        return returnName;
    }
    public Long checkWeightPerson(String weight){

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
    public String checkPassportIdPerson(String passport){
        String returnPassportId = null;
        returnPassportId = passport;
        if (returnPassportId == null || returnPassportId.isEmpty() || returnPassportId.replaceAll(" ", "").replaceAll("\t", "").length() == 0 || returnPassportId.length() > 255){
            return null;
        }
        return returnPassportId;
    }
}