package exception;

/**
 * Исключение срабатывает, если значение поля превышает допустимое
 */

public class NumberLongerException extends CustomException {
    public NumberLongerException(Number number){
        setMessage(String.format("Максимальное значение: %d", number));
    }
}
