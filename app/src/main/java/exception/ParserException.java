package exception;

/**
 * Исключение срабатывает, если во время работы с данными произошла ошибка
 */

public class ParserException extends CustomException{
    public ParserException() {
        setMessage("Во время парсинга данных произошла ошибка");
    }
}
