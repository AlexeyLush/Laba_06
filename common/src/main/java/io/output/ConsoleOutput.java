package io.output;

import io.enums.ColorConsole;

/**
 * Консоль для вывода
 */

public interface ConsoleOutput{
    void output(Object message);
    void outputln(Object message);
    void outputWithColor(Object message, ColorConsole color);
}
