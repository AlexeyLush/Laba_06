package dao;

import models.LabWork;

/**
 * Интерфейс для работы DAO
 */

import java.util.Map;

public interface DAO<K extends Comparable,V extends Comparable> {
    int create(K key, V labWork);
    void update(int id, V labWork);
    void delete(K key);
    void clear();
    V get(K key);
}
