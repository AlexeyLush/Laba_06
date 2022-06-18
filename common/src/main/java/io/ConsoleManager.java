package io;

import io.enums.ColorConsole;
import io.output.ConsoleOutput;
import io.output.MessageOutput;

import java.io.Console;

/**
 * Класс для работы с консолью
 */

public class ConsoleManager implements ConsoleOutput, MessageOutput {

    private boolean isColorConsole;
    private boolean logMode;
    private Console console;

    public ConsoleManager(boolean isColorConsole, boolean logMode) {
        this.isColorConsole = isColorConsole;
        this.logMode = logMode;
        this.console = System.console();
    }

    public Console getConsole() {
        return this.console;
    }

    public void setColorConsole(boolean colorConsole) {
        isColorConsole = colorConsole;
    }

    @Override
    public void output(Object message) {
        System.out.print(message);
    }

    @Override
    public void outputln(Object message) {
        System.out.println(message);
    }

    @Override
    public void outputWithColor(Object message, ColorConsole color){
        if (isColorConsole){
            System.out.println(color + message.toString() + ColorConsole.ANSI_RESET);
        }
        else{
            System.out.println(message);
        }
    }

    @Override
    public void info(Object message) {
        if (logMode){
            outputln("[INFO]: " + message);
        }
        else{
            outputln(message);
        }
    }

    @Override
    public void error(Object message) {
        if (logMode){
            outputWithColor("[ERROR]: " + message, ColorConsole.ANSI_RED);
        } else{
            outputWithColor(message, ColorConsole.ANSI_RED);
        }

    }

    @Override
    public void warning(Object message) {
        if (logMode){
            outputWithColor("[WARN]: " + message, ColorConsole.ANSI_YELLOW);
        } else {
            outputWithColor(message, ColorConsole.ANSI_YELLOW);
        }

    }

    @Override
    public void successfully(Object message) {
        if (logMode){
            outputWithColor("[SUCCESSFULLY]: " + message, ColorConsole.ANSI_GREEN);
        }
        else {
            outputWithColor(message, ColorConsole.ANSI_GREEN);
        }
    }

}
