package services.parsers.interfaces;

import java.util.Map;

/**
 * Интерфейс для парсинга коллекции
 */

public interface ParserMap<K,V> {
    Map<K,V> deserializeMap(String json);
    String serializeMap(Map<K,V> elements);
}
