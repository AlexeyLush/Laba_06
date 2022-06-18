package interactive;

import io.ConsoleManager;
import request.Request;
import response.Response;

import java.util.Scanner;

public class InputInteractiveCommand {

    public Request inputData(String authorization, ConsoleManager consoleManager, Scanner scanner, Response response) {

        if (response.statusCode == 400){
            if (response.argument != null){
                consoleManager.error(response.argument.toString());
            }
        }


        if (response.message != null){
            consoleManager.output(response.message);
        }

        String data = scanner.nextLine();
        Request request = new Request();
        request.message = response.command;
        request.element = data;
        request.authorization = authorization;
        return request;
    }

}
