package services.parsers.interfaces;

/**
 * Интерфейс для парсинга элементов
 */

public interface ParserElement<T> {

    T deserializeElement(String json);
    String serializeElement(T elements);

}
