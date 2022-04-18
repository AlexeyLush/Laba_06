package io.output;

import io.enums.ColorConsole;

/**
 * Консоль для вывода
 */

public interface ConsoleOutput{
    void output(String message);
    void outputln(String message);
    void outputWithColor(String message, ColorConsole color);
}
