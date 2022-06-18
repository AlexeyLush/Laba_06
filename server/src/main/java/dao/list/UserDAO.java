package dao.list;

import database.DatabaseInfo;
import database.PostgresDatabase;
import models.User;

import java.sql.*;
import java.util.Properties;


public class UserDAO implements DAO{

    private PostgresDatabase database;
    private Driver driver;

    public UserDAO(PostgresDatabase database) {
        this.database = database;
        this.driver = PostgresDatabase.getDriver();
    }
    public int insert(User user) {
        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        int id = 0;

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            String script = "INSERT INTO Users(id, name, hash, salt) VALUES(nextval('id'),?,?,?) RETURNING id";
            PreparedStatement statement = connection.prepareStatement(script);
            statement.setString(1, user.login);
            statement.setString(2, user.hash);
            statement.setString(3, user.salt);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            id = resultSet.getInt("id");

        } catch (SQLException e) {
            String code = e.getSQLState();
            if (code.equals("23505")){
                return -1;
            }
            return 0;

        }
        return id;
    }
    public String getSaltOfUser(User user) {
        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            String script = "SELECT salt FROM Users WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(script);
            statement.setString(1, user.login);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            String salt = resultSet.getString("salt");

            return salt;

        } catch (SQLException e) {
            String code = e.getSQLState();
            return null;
        }
    }
    public int login(User user){
        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            String script = "SELECT hash, salt FROM Users WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(script);
            statement.setString(1, user.login);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();

            String hash = resultSet.getString("hash");

            if (!hash.equals(user.hash)) {
                return -1;
            }

            return 1;

        } catch (SQLException e) {
            return 0;
        }
    }
    public int getUserIdByName(String name) {

        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            String scriptGetUserId = "SELECT id FROM Users WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(scriptGetUserId );
            statement.setString(1, name);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            return resultSet.getInt("id");

        } catch (SQLException e) {
            return 0;
        }
    }

    public String getUserNameById(int id) {
        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            String scriptGetUserId = "SELECT name FROM Users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(scriptGetUserId );
            statement.setInt(1, id);
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            return resultSet.getString("name");

        } catch (SQLException e) {
            return null;
        }
    }


}
