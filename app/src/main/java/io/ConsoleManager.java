package io;

import io.enums.ColorConsole;
import io.output.ConsoleOutput;
import io.output.MessageOutput;

/**
 * Класс для работы с консолью
 */

public class ConsoleManager implements ConsoleOutput, MessageOutput {

    @Override
    public void output(String message) {
        System.out.print(message);
    }

    @Override
    public void outputln(String message) {
        System.out.println(message);
    }

    @Override
    public void outputWithColor(String message, ColorConsole color){
        System.out.println(color + message + ColorConsole.ANSI_RESET);
    }

    @Override
    public void info(String message) {
        output(message);
    }

    @Override
    public void error(String message) {
        outputWithColor(message, ColorConsole.ANSI_RED);
    }

    @Override
    public void warning(String message) {
        outputWithColor(message, ColorConsole.ANSI_YELLOW);
    }

    @Override
    public void successfully(String message) {
        outputWithColor(message, ColorConsole.ANSI_GREEN);
    }

}
