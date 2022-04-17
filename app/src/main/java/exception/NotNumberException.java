package exception;

/**
 * Исключение срабатывает, если пользователь не ввёл число
 */

public class NotNumberException extends CustomException{
    public NotNumberException() {
        setMessage("Введите число");
    }
}
