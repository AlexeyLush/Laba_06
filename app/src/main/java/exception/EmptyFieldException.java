package exception;

/**
 * Исключение срабатывает, когда пользователь не ввёл значение
 */

public class EmptyFieldException extends CustomException{
    public EmptyFieldException() {
        setMessage("Вы не ввели значение");
    }
}
