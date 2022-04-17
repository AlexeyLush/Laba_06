package exception;

/**
 * Исключение срабатывает, если ключ, введённый пользователем уже занят
 */

public class NotUniqueKeyException extends CustomException {
    public NotUniqueKeyException(String  key) {
        setMessage(String.format("Ключ %s уже занят", key));
    }
}
