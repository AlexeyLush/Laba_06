package dao.map;

import database.DatabaseInfo;
import database.PostgresDatabase;
import models.Difficulty;
import models.LabWork;
import models.service.GenerationID;
import service.token.TokenGenerator;

import javax.xml.crypto.Data;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Класс для реализации работы с коллекцией LabWork
 */

public class LabWorkDAO implements DAOMap<String, LabWork>, MapWork<String, LabWork> {

    private Map<String, LabWork> labWorkList = new LinkedHashMap<>();
    private Driver driver;
    private PostgresDatabase database;

    public LabWorkDAO(PostgresDatabase database) {
        this.database = database;
        this.driver = PostgresDatabase.getDriver();
    }

    public int create(String key, LabWork labWork, String token) {

        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            int idCoordinates = database.getCoordinatesDAO().insert(labWork.getCoordinates());
            int idPerson = database.getPersonsDAO().insert(labWork.getAuthor());

            String userName = TokenGenerator.decodeToken(token).userName;
            int idUser = database.getUserDAO().getUserIdByName(userName);

            String script = "INSERT INTO LabWorks(id, key, name, coordinates_id, creationdate, minimalpoint, " +
                    "description, difficulty, person_id, user_id) " +
                    "VALUES (nextval('id'), ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id";
            PreparedStatement statement = connection.prepareStatement(script);
            statement.setString(1, key);
            statement.setString(2, labWork.getName());
            statement.setInt(3, idCoordinates);
            statement.setString(4, labWork.getCreationDate()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            statement.setDouble(5, labWork.getMinimalPoint());
            statement.setString(6, labWork.getDescription());
            statement.setString(7, labWork.getDifficulty().getValue());
            statement.setInt(8, idPerson);
            statement.setInt(9, idUser);

            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            return resultSet.getInt("id");


        } catch (SQLException e) {
            String code = e.getSQLState();
            if (code.equals("23505")){
                return -1;
            }
            return 0;
        }
    }

    @Override
    public int update(int id, LabWork labWork, String token) {

        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            int idCoordinates = database.getCoordinatesDAO().insert(labWork.getCoordinates());
            int idPerson = database.getPersonsDAO().insert(labWork.getAuthor());

            String userName = TokenGenerator.decodeToken(token).userName;
            int idUser = database.getUserDAO().getUserIdByName(userName);

            if (database.getCoordinatesDAO().update(idCoordinates, labWork.getCoordinates()) < 1){
                throw new SQLException();
            }
            if (database.getPersonsDAO().update(idPerson, labWork.getAuthor()) < 1){
                throw new SQLException();
            }

            String script = """
                    UPDATE LabWorks 
                    SET name = ?, coordinates_id = ?, minimalpoint = ?,
                    description = ?, difficulty = ?, person_id = ?
                    WHERE id = ?
                    """;
            PreparedStatement statement = connection.prepareStatement(script);

            statement.setString(1, labWork.getName());
            statement.setInt(2, idCoordinates);
            statement.setDouble(3, labWork.getMinimalPoint());
            statement.setString(4, labWork.getDescription());
            statement.setString(5, labWork.getDifficulty().getValue());
            statement.setInt(6, idPerson);
            statement.setInt(7, id);

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

    @Override
    public int delete(String key) {


        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            String script = """
                            DELETE FROM LabWorks
                            WHERE key = ?
                            RETURNING id
                            """;
            PreparedStatement preparedStatement = connection.prepareStatement(script);
            preparedStatement.setString(1, key);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            resultSet.next();

            return resultSet.getInt("id");

        } catch (SQLException e) {
            return 0;
        }


    }

    @Override
    public void initialMap(Map<String, LabWork> elements) {
        this.labWorkList = elements;
    }

    public int setLabWorksFromDatabase() {

        labWorkList = new LinkedHashMap<>();
        Properties properties = new Properties();
        properties.put("user", DatabaseInfo.USER);
        properties.put("password", DatabaseInfo.PASSWORD);

        try (Connection connection = driver.connect(DatabaseInfo.CONNECTION_STRING, properties)) {

            String script = "SELECT * FROM LabWorks";


            PreparedStatement statement = connection.prepareStatement(script);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();


            while (resultSet.next()) {
                String key;
                LabWork labWork = new LabWork();

                key = resultSet.getString("key");
                labWork.setId(resultSet.getInt("id"));
                labWork.setName(resultSet.getString("name"));
                labWork.setCoordinates(database.getCoordinatesDAO()
                        .getCoordinateById(resultSet.getInt("coordinates_id")));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

                LocalDateTime date = LocalDateTime.parse(resultSet.getString("creationdate"), formatter);

                ZonedDateTime resultDate = date.atZone(ZoneId.systemDefault());

                labWork.setCreationDate(resultDate);
                labWork.setMinimalPoint(resultSet.getFloat("minimalpoint"));
                labWork.setDescription(resultSet.getString("description"));
                labWork.setDifficulty(Difficulty.isDifficulty(resultSet.getString("difficulty")));
                labWork.setAuthor(database.getPersonsDAO().
                        getPersonById(resultSet.getInt("person_id")));
                labWork.setUserName(database.getUserDAO()
                        .getUserNameById(resultSet.getInt("user_id")));

                labWorkList.put(key, labWork);

            }

            return 1;



        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public void clear() {
        labWorkList.clear();
    }

    @Override
    public Map<String, LabWork> sort(Map<String, LabWork> map) {

        return labWorkList
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    @Override
    public LabWork get(String key) {
        return labWorkList.get(key);
    }

    @Override
    public Map<String, LabWork> getAll() {
        return new LinkedHashMap<>(sort(labWorkList));
    }
}
