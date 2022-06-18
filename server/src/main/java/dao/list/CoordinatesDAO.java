package dao.list;

import database.DatabaseInfo;
import database.PostgresDatabase;
import models.Coordinates;
import org.checkerframework.checker.units.qual.C;
import service.token.TokenGenerator;

import java.sql.*;
import java.util.Properties;

public class CoordinatesDAO {

    private PostgresDatabase database;
    private Driver driver;

    public CoordinatesDAO(PostgresDatabase database) {
        this.database = database;
        this.driver = PostgresDatabase.getDriver();
    }

    public Coordinates getCoordinateById(int id) {
        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            String scriptGetCoordinatesById = "SELECT x, y FROM Coordinates WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(scriptGetCoordinatesById);
            statement.setInt(1, id);

            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            resultSet.next();

            Coordinates coordinates = new Coordinates();
            coordinates.setX(resultSet.getLong("x"));
            coordinates.setY(resultSet.getInt("y"));

            return coordinates;

        } catch (SQLException e) {
            return null;
        }
    }

    public int update(int id, Coordinates coordinates) {
        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            String script = """
                    UPDATE Coordinates 
                    SET x = ?, y = ?
                    WHERE id = ?
                    """;
            PreparedStatement statement = connection.prepareStatement(script);

            statement.setLong(1, coordinates.getX());
            statement.setInt(2, coordinates.getY());
            statement.setInt(3, id);

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

    public int insert(Coordinates coordinates) {
        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            String scriptCoordinates = "INSERT INTO Coordinates(id, x, y) VALUES (nextval('id'), ?, ?) returning id";
            PreparedStatement statement = connection.prepareStatement(scriptCoordinates);
            statement.setDouble(1, coordinates.getX());
            statement.setLong(2, coordinates.getY());
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            return resultSet.getInt("id");

        } catch (SQLException e) {
            return 0;
        }
    }

}


