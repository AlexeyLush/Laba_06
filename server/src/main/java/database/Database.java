package database;

import java.util.List;

public interface Database<T> {
    void createDatabase();
    void writeToDatabase(String script);
    T getElement(String script);
    List<T> getElements(String script);
}
