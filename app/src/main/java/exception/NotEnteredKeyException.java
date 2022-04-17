package exception;

/**
 * Исключение срабатывает, если пользователь не ввёл ключ
 */

public class NotEnteredKeyException extends CustomException{
    public NotEnteredKeyException(){
        setMessage("Вы не ввели ключ");
    }
}
