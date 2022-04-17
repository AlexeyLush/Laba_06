package exception;

/**
 * Исключение срабатывает, если значение поля меньше, чем допустимое
 */

public class NumberMinimalException extends CustomException{
    public NumberMinimalException(Number number){
        setMessage(String.format("Число должно быть больше %d", number));
    }
}
