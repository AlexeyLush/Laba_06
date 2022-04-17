package exception;

/**
 * Исключение срабатывает когда коллекция пуста
 */

public class EmptyCollection extends CustomException{
    public EmptyCollection(){
        setMessage("коллекция пустая..........");
    }
}
