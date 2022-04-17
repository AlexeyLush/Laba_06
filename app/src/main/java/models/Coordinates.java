package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Класс координат
 */

public class Coordinates {
    private Long x; //Максимальное значение поля: 713, Поле не может быть null
    private Integer y;
    @JsonIgnore
    private final Long maxCoordinateX = 713L;

    public Coordinates(){
        x = null;
        y = null;
    }

    public Coordinates(Long x, Integer y) {
        this.x = x;
        this.y = y;
    }


    // Геттеры
    public Long getX(){
        return x;
    }
    public Integer getY(){
        return y;
    }
    public Long getMaxCoordinateX(){
        return maxCoordinateX;
    }
    // Сеттеры
    public void setX(Long x){
        this.x = x;
    }
    public void setY(Integer y){
        this.y = y;
    }
    // toString
    public String toString(){
        return "Coordinates: {" + "x = " + x + ", y = " + y + "}";
    }
}
