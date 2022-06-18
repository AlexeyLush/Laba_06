package database;

import dao.list.CoordinatesDAO;
import dao.list.PersonsDAO;
import dao.list.UserDAO;
import dao.map.LabWorkDAO;
import io.ConsoleManager;
import models.LabWork;

import java.sql.*;
import java.util.List;
import java.util.Properties;

public class PostgresDatabase implements Database<LabWork> {

    private ConsoleManager consoleManager;

    private LabWorkDAO labWorkDAO;
    private UserDAO userDAO;
    private CoordinatesDAO coordinatesDAO;
    private PersonsDAO personsDAO;

    public PostgresDatabase(ConsoleManager consoleManager) {
        this.consoleManager = consoleManager;
        this.labWorkDAO = new LabWorkDAO(this);
        this.userDAO = new UserDAO(this);
        this.coordinatesDAO = new CoordinatesDAO(this);
        this.personsDAO = new PersonsDAO(this);
    }



    public ConsoleManager getConsoleManager() {
        return consoleManager;
    }

    public LabWorkDAO getLabWorkDAO() {
        return labWorkDAO;
    }

    public CoordinatesDAO getCoordinatesDAO() {
        return coordinatesDAO;
    }

    public PersonsDAO getPersonsDAO() {
        return personsDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    @Override
    public void createDatabase() {
        String scriptForCreateDatabase = """
                
                CREATE SEQUENCE id START 1;
                
                CREATE TABLE Persons (
                    id INTEGER PRIMARY KEY UNIQUE NOT NULL,
                    name VARCHAR(255) NOT NULL,
                    weight bigint NOT NULL CHECK (weight > 0),
                    passportID VARCHAR(255) NOT NULL
                );
                                               
                CREATE TABLE Coordinates(
                    id INTEGER PRIMARY KEY UNIQUE NOT NULL,
                    x decimal NOT NULL CHECK (x <= 713),
                    y BIGINT NOT NULL
                );
                
                CREATE TABLE Users(
                    id INTEGER PRIMARY KEY UNIQUE NOT NULL,
                    name VARCHAR(255) NOT NULL UNIQUE,
                    hash VARCHAR(512) NOT NULL,
                    salt VARCHAR(255) NOT NULL
                );
                
                CREATE TABLE LabWorks(
                    id INTEGER PRIMARY KEY UNIQUE NOT NULL,
                    key VARCHAR(255) UNIQUE NOT NULL,
                    name VARCHAR(255) NOT NULL,
                    coordinates_id INTEGER NOT NULL,
                    creationDate VARCHAR(255) NOT NULL,
                    minimalPoint BIGINT NOT NULL,
                    description VARCHAR(255) NOT NULL,
                    difficulty VARCHAR(255) NOT NULL,
                    person_id INTEGER NOT NULL,
                    user_id INTEGER NOT NULL
                );

                
                ALTER TABLE LabWorks
                ADD CONSTRAINT FK_CoordinatesLabWork
                FOREIGN KEY (coordinates_id) REFERENCES Coordinates(id);
                
                ALTER TABLE LabWorks
                ADD CONSTRAINT FK_UserLabWork
                FOREIGN KEY (user_id) REFERENCES Users(id);
                
                ALTER TABLE LabWorks
                ADD CONSTRAINT FK_PersonsLabWork
                FOREIGN KEY (person_id) REFERENCES Persons(id);

                """;
        writeToDatabase(scriptForCreateDatabase);
    }

    @Override
    public void writeToDatabase(String script) {
        Driver driver = getDriver();
        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            PreparedStatement statement = connection.prepareStatement(script);
            statement.execute();

        } catch (SQLException e) {
            consoleManager.error(e.getMessage());
        }
    }

    @Override
    public LabWork getElement(String script) {
        return null;
    }

    @Override
    public List<LabWork> getElements(String script) {
        return null;
    }

    public static Driver getDriver() {
        try{
            return DriverManager.getDriver(DatabaseInfo.CONNECTION_STRING);
        } catch (SQLException e) {
            return null;
        }
    }
}
