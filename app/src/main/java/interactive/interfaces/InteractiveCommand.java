package interactive.interfaces;

import io.ConsoleManager;
import request.Request;
import response.Response;

import java.util.Scanner;

public interface InteractiveCommand {
    Request inputData(ConsoleManager consoleManager, Scanner scanner, Response response);
}
