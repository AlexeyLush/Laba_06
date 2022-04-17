package exception;

public class NotFoundKeyException extends CustomException{
    public NotFoundKeyException(){
        setMessage("Ключ не может быть пустым");
    }
}