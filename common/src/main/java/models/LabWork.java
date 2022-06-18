package models;


import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


/**
 * Класс лабораторных работ
 */
public class LabWork implements Comparable<LabWork>{
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Float minimalPoint; //Поле может быть null, Значение поля должно быть больше 0
    private String description; //Строка не может быть пустой, Поле не может быть null
    private Difficulty difficulty; //Поле может быть null
    private Person author; //Поле может быть null
    private String userName;

    public LabWork(){
        this.creationDate = ZonedDateTime.now();
    }

    public LabWork(int id, String name, Coordinates coordinates, ZonedDateTime creationDate, Float minimalPoint, String description, Difficulty difficulty, Person author) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.minimalPoint = minimalPoint;
        this.description = description;
        this.difficulty = difficulty;
        this.author = author;
    }


    // Геттеры и сеттеры

    public void setId(int id){
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Float getMinimalPoint() {
        return minimalPoint;
    }

    public void setMinimalPoint(Float minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    // Переопределение метода toString


    @Override
    public String toString() {
        return "ID: " + id + System.getProperty("line.separator")
                + "Дата создания: " + creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + System.getProperty("line.separator")
                + "Название работы: " + name + System.getProperty("line.separator")
                + "Координата X: " + coordinates.getX() + System.getProperty("line.separator")
                + "Координата Y: " + coordinates.getY() + System.getProperty("line.separator")
                + "Минимальная точка: " + minimalPoint + System.getProperty("line.separator")
                + "Описание: " + description + System.getProperty("line.separator")
                + "Сложность: " + difficulty + System.getProperty("line.separator")
                + "Имя автора: " + author.getName() + System.getProperty("line.separator")
                + "Вес: " + author.getWeight() + System.getProperty("line.separator")
                + "ID паспорта: " + author.getPassportID() + System.getProperty("line.separator")
                + "Добавил пользователь: " + userName + System.getProperty("line.separator");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabWork labWork = (LabWork) o;
        return id == labWork.id && Objects.equals(name, labWork.name) && Objects.equals(coordinates, labWork.coordinates) && Objects.equals(creationDate, labWork.creationDate) && Objects.equals(minimalPoint, labWork.minimalPoint) && Objects.equals(description, labWork.description) && difficulty == labWork.difficulty && Objects.equals(author, labWork.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, minimalPoint, description, difficulty, author);
    }

    @Override
    public int compareTo(LabWork labWork) {
        return this.id - labWork.getId();
    }
}
