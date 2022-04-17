package exception;

/**
 * Исключение срабатывает, когда id, указанный в команде, не найдем
 */

public class IdNotFoundException extends CustomException{
    public IdNotFoundException(){
        setMessage("Элмент с таким id не найден");
    }
}
