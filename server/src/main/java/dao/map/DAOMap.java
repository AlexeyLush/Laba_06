package dao.map;

/**
 * Интерфейс для работы DAO
 */

public interface DAOMap<K extends Comparable,V extends Comparable> {
    int update(int id, V labWork, String token);
    int delete(K key);
    void clear();
    V get(K key);
}
