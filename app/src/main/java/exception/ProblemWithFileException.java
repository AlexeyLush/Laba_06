package exception;

/**
 * Исключение срабатывает, если во время работы программы возникла проблема с файлом
 */

public class ProblemWithFileException extends CustomException{
    public ProblemWithFileException(){
        setMessage("Во время работы программы возникла проблема с файлом");
    }
}
