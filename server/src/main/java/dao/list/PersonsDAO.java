package dao.list;

import database.DatabaseInfo;
import database.PostgresDatabase;
import models.Coordinates;
import models.Person;
import models.User;

import java.sql.*;
import java.util.Properties;

public class PersonsDAO {

    private PostgresDatabase database;
    private Driver driver;

    public PersonsDAO(PostgresDatabase database) {
        this.database = database;
        this.driver = PostgresDatabase.getDriver();
    }

    public int insert(Person person) {
        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            String scriptPerson = "INSERT INTO Persons(id, name, weight, passportid) VALUES (nextval('id'), ?, ?, ?) returning id";
            PreparedStatement statement = connection.prepareStatement(scriptPerson);
            statement.setString(1, person.getName());
            statement.setLong(2, person.getWeight());
            statement.setString(3, person.getPassportID());
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            return resultSet.getInt("id");

        } catch (SQLException e) {
            return 0;
        }
    }

    public int update(int id, Person person){
        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            String script = """
                    UPDATE Persons
                    SET name = ?, weight = ?, passportid = ?
                    WHERE id = ?
                    """;
            PreparedStatement statement = connection.prepareStatement(script);

            statement.setString(1, person.getName());
            statement.setLong(2, person.getWeight());
            statement.setString(3, person.getPassportID());
            statement.setInt(4, id);

            statement.execute();

            return 1;

        } catch (SQLException e) {
            String code = e.getSQLState();
            if (code.equals("23505")){
                return -1;
            }
            return 0;
        }
    }

    public Person getPersonById(int id) {
        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            String scriptGetPersonById = "SELECT name, weight, passportid FROM Persons WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(scriptGetPersonById);
            statement.setInt(1, id);
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            resultSet.next();

            Person person = new Person();

            person.setName(resultSet.getString("name"));
            person.setWeight(resultSet.getLong("weight"));
            person.setPassportID(resultSet.getString("passportid"));

            return person;

        } catch (SQLException e) {
            return null;
        }
    }

}
