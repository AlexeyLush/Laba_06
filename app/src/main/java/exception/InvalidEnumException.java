package exception;

/**
 * Исключние срабатывает, когда ввод не ссответсвует перечислению
 */

public class InvalidEnumException extends CustomException{
    @Override
    public String toString(){
        return "ввод не соответсвует перечислению";
    }
}
