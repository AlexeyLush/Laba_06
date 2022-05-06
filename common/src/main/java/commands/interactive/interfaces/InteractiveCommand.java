package commands.interactive.interfaces;

import io.ConsoleManager;
import models.LabWork;
import request.Request;
import response.Response;

import java.util.Map;
import java.util.Scanner;

public interface InteractiveCommand {
    Request inputData(ConsoleManager consoleManager, Scanner scanner, Response response);
}
