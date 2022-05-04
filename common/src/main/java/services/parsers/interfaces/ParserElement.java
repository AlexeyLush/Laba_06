package services.parsers.interfaces;

/**
 * Интерфейс для парсинга элементов
 */

public interface ParserElement {

    <T> T deserializeElement(String json, Class<T> tClass);
    String serializeElement(Object elements);

}
