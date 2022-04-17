package services.spliters.interfaces;

import io.ConsoleManager;

public interface SplitCommand {
    String[] splitedCommand(String command, ConsoleManager consoleManager);
}
