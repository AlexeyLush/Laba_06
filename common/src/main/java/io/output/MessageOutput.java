package io.output;

/**
 * Интерфейс для вывода сообщения
 */

public interface MessageOutput {
    void info(Object message);
    void error(Object message);
    void warning(Object message);
    void successfully(Object message);
}
