package exception;

/**
 * Исключение срабатывает, если элемент не найден
 */

public class NotFoundElementException extends CustomException{
    public NotFoundElementException(){
        setMessage("Элемент не найден");
    }
}
