package dao;

/**
 * Интерфейс для работы DAO
 */

public interface DAO<K extends Comparable,V extends Comparable> {
    int create(K key, V labWork);
    void update(int id, V labWork);
    void delete(K key);
    void clear();
    V get(K key);
}
