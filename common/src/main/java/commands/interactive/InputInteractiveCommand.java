package commands.interactive;

import io.ConsoleManager;
import request.Request;
import response.Response;

import java.util.Scanner;

public class InputInteractiveCommand {

    public Request inputData(ConsoleManager consoleManager, Scanner scanner, Response response) {


        if (response.status == Response.Status.ERROR){
            if (response.argument != null){
                consoleManager.error(response.argument.toString());
            }
        }


        if (response.message != null){
            consoleManager.output(response.message);
        }


        String data = scanner.nextLine();
        Request request = new Request();
        request.commandName = response.command;
        request.element = data;
        return request;
    }

}
