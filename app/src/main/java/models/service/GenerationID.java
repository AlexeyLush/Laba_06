package models.service;

/**
 * Генератор id
 */

public class GenerationID {
    private static int id = 1;

    public static Integer newId(){
        return id++;
    }

    public static void setId(int id){
        GenerationID.id = id;
    }
}
