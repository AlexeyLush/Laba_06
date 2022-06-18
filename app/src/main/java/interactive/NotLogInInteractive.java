package interactive;

import io.ConsoleManager;

import java.util.Scanner;

public class NotLogInInteractive {
    public InputType inputData(ConsoleManager consoleManager, Scanner scanner){


        consoleManager.outputln("login : войти в систему");
        consoleManager.outputln("register : зарегистрироваться в системе");
        consoleManager.outputln("exit : выйти из программы");

        consoleManager.outputln("");

        InputType inputType;
        while (true){
            consoleManager.output("Введите команду: ");
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("login")){
                inputType = InputType.LOGIN;
                break;
            } else if (command.equalsIgnoreCase("register")){
                inputType = InputType.REGISTER;
                break;
            } else if (command.equalsIgnoreCase("exit")){
                inputType = InputType.EXIT;
                break;
            }
            else {
                consoleManager.error("Команда не найдена");
            }
        }

        return inputType;


    }
}
