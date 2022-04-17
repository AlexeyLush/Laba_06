package exception;

public class NotFoundEnumException extends CustomException{
    public NotFoundEnumException(){
        setMessage("Элемент не найден");
    }
}
