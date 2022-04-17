package exception;

/**
 * Исключение срабатывает, если ввёд неправильный аргумнт команды
 */

public class MissedCommandArgumentException extends CustomException{
    @Override
    public String toString(){
        return "неправильный аргумент команды";
    }
}
