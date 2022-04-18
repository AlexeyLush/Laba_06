package io.output;

/**
 * Интерфейс для вывода сообщения
 */

public interface MessageOutput {
    void info(String message);
    void error(String message);
    void warning(String message);
    void successfully(String message);
}
