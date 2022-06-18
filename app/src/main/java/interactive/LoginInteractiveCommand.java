package interactive;

import io.ConsoleManager;
import models.Person;
import models.User;
import request.Request;
import services.hashing.HashPassword;
import services.hashing.SaltGenerator;
import services.parsers.ParserJSON;

import javax.swing.*;
import java.util.Arrays;
import java.util.Scanner;

public class LoginInteractiveCommand {

    private User user;
    private String password;

    public LoginInteractiveCommand() {
        this.user = new User();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    private boolean checkPassword(String password, ConsoleManager consoleManager) {
        if (password.length() == 0){
            consoleManager.error("Вы не ввели пароль");
            return false;
        }
        return true;
    }
    private boolean checkLogin(String login, ConsoleManager consoleManager) {
        if (login.length() == 0) {
            consoleManager.error("Вы не ввели логин");
            return false;
        }
        return true;
    }
    private String inputPassword(ConsoleManager consoleManager, Scanner scanner){
        consoleManager.output("Введите пароль: ");
        String password = scanner.nextLine();

        while (!checkPassword(password, consoleManager)){
            consoleManager.output("Введите пароль: ");
            password = scanner.nextLine();
        }

        return password;
    }
    private String inputPassword(ConsoleManager consoleManager){
        consoleManager.output("Введите пароль: ");
        String password = Arrays.toString(consoleManager.getConsole().readPassword());

        while (!checkPassword(password, consoleManager)){
            consoleManager.output("Введите пароль: ");
            password = Arrays.toString(consoleManager.getConsole().readPassword());
        }
        return password;
    }
    private String inputLogin(ConsoleManager consoleManager, Scanner scanner) {

        consoleManager.output("Введите логин: ");
        String login = scanner.nextLine();

        while (!checkLogin(login, consoleManager)){
            consoleManager.output("Введите логин: ");
            login = scanner.nextLine();
        }

        return login;
    }

    private Request createRequest(String login, String password) {

        this.user.login = login;
        this.password = password;

        Request request = new Request();
        request.authorization = null;
        request.method = "POST";
        request.host = "localhost";
        request.element = login;
        request.contentType = "application/json";
        request.contentLength = request.element.toString().length();
        request.message = InputType.GET_SALT.getType();

        return request;
    }

    public Request inputData(ConsoleManager consoleManager, Scanner scanner){
        String login = inputLogin(consoleManager, scanner);
        String password;

        if (consoleManager.getConsole() != null){
            password = inputPassword(consoleManager);
        }
        else {
            password = inputPassword(consoleManager, scanner);
        }

        return createRequest(login, password);
    }
}
