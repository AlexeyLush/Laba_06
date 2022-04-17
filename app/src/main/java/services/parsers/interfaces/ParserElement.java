package services.parsers.interfaces;

import java.util.Map;

/**
 * Интерфейс для парсинга элементов
 */

public interface ParserElement<T> {

    T deserializeElement(String json);
    String serializeElement(T elements);

}
