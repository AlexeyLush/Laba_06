package io;

import io.enums.ColorConsole;
import io.output.ConsoleOutput;
import io.output.MessageOutput;

/**
 * Класс для работы с консолью
 */

public class ConsoleManager implements ConsoleOutput, MessageOutput {

    private boolean isColorConsole;

    public ConsoleManager(boolean isColorConsole) {
        this.isColorConsole = isColorConsole;
    }

    public void setColorConsole(boolean colorConsole) {
        isColorConsole = colorConsole;
    }

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
        if (isColorConsole){
            System.out.println(color + message + ColorConsole.ANSI_RESET);
        }
        else{
            System.out.println(message);
        }
    }

    @Override
    public void info(String message) {
        outputln("[INFO]: " + message);
    }

    @Override
    public void error(String message) {
        outputWithColor("[ERROR]: " + message, ColorConsole.ANSI_RED);
    }

    @Override
    public void warning(String message) {
        outputWithColor("[WARN]: " + message, ColorConsole.ANSI_YELLOW);
    }

    @Override
    public void successfully(String message) {
        outputWithColor("[SUCCESSFULLY]: " + message, ColorConsole.ANSI_GREEN);
    }

}
