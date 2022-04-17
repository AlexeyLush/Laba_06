package exception;

/**
 * Исключение срабатывает, если введённая команда не найдена
 */

public class NotFoundCommandException extends CustomException {
    public NotFoundCommandException(){
        setMessage("Команда не найдена");
    }
}
