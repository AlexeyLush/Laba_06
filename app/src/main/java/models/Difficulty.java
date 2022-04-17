package models;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * Перечисляемые уровни сложности
 */

public enum Difficulty {
    VERY_EASY("VERY_EASY"),
    NORMAL("NORMAL"),
    VERY_HARD("VERY_HARD"),
    HOPELESS("HOPELESS"),
    TERRIBLE("TERRIBLE");

    private final String difficulty;

    public static Map<String, Difficulty> getMapDifficulties(){

        Map<String, Difficulty> difficultyMap = new LinkedHashMap<>();
        Difficulty[] difficulties = Difficulty.values();
        for (Difficulty value : difficulties) {
            difficultyMap.put(value.getValue(), value);
        }
        return difficultyMap;
    }

    public static Difficulty isDifficulty(String difficultyString){
        if (getMapDifficulties().containsKey(difficultyString.toUpperCase())){
            return getMapDifficulties().get(difficultyString.toUpperCase());
        }
        return null;
    }

    public String getValue(){
        return this.difficulty;
    }

    Difficulty(String difficulty){
        this.difficulty = difficulty;
    }

}
