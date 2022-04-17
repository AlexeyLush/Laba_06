package exception;

/**
 * Исключение срабатывает, если пользователь ввёл значение поля, меньше 0
 */

public class NotPositiveNumberException extends CustomException{
    public NotPositiveNumberException(){
        setMessage("Значение поля должно быть больше 0");
    }
}
