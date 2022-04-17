package exception;

public class NotFoundScriptFileException extends CustomException{
    public NotFoundScriptFileException(){
        setMessage("Файл со скриптом не найден");
    }
}
