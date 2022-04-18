package dao;

import java.util.Map;

public interface MapWork<K extends Comparable,V extends Comparable> {
    void initialMap(Map<K, V> elements);
    Map<K, V> sort(Map<K, V> map);
    Map<K, V> getAll();
}
