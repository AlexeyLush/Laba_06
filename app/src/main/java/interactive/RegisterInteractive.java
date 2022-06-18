package interactive;

import io.ConsoleManager;
import models.Person;
import models.User;
import request.Request;
import services.hashing.HashPassword;
import services.hashing.SaltGenerator;
import services.parsers.ParserJSON;

import java.io.Console;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;

public class RegisterInteractive {

    static class Passwords {

        private final String password;
        private final String replyPassword;

        public Passwords (String password, String replyPassword) {
            this.password = password;
            this.replyPassword = replyPassword;
        }
    }

    private boolean checkPassword(String password, ConsoleManager consoleManager) {
        if (password.length() < 6 || password.replace(" ", "").replace("\t", "").length() == 0){
            consoleManager.error("Пароль должен содержать более 6 символов");
            return false;
        }
        return true;
    }

    private boolean checkLogin(String login, ConsoleManager consoleManager) {
        if (login.contains(" ") || login.contains("\t")){
            consoleManager.error("Логин не должен содержать пустые символы");
            return false;
        }
        if (login.length() < 5) {
            consoleManager.error("Логин должен быть больше 5 символов");
            return false;
        }
        return true;
    }


    private Passwords inputPassword(ConsoleManager consoleManager, Scanner scanner){
        consoleManager.output("Введите пароль: ");
        String password = scanner.nextLine();

        while (!checkPassword(password, consoleManager)){
            consoleManager.output("Введите пароль: ");
            password = scanner.nextLine();
        }

        consoleManager.output("Повторите пароль: ");
        String replyPassword = scanner.nextLine();

        return new Passwords(password, replyPassword);
    }
    private Passwords inputPassword(ConsoleManager consoleManager){
        consoleManager.output("Введите пароль: ");
        String password = Arrays.toString(consoleManager.getConsole().readPassword());

        while (!checkPassword(password, consoleManager)){
            consoleManager.output("Введите пароль: ");
            password = Arrays.toString(consoleManager.getConsole().readPassword());
        }
        consoleManager.output("Повторите пароль: ");
        String replyPassword = Arrays.toString(consoleManager.getConsole().readPassword());
        return new Passwords(password, replyPassword);
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

        String salt = SaltGenerator.getSalt();
        String hashPassword = HashPassword.getHashPassword(password, salt);

        User user = new User();
        user.login = login;
        user.hash = hashPassword;
        user.salt = salt;

        Request request = new Request();
        request.authorization = null;
        request.method = "POST";
        request.host = "localhost";
        request.element = new ParserJSON().serializeElement(user);
        request.contentType = "application/json";
        request.contentLength = request.element.toString().length();
        request.message = InputType.REGISTER_ACCOUNT.getType();

        return request;

    }

    public Request inputData(ConsoleManager consoleManager, Scanner scanner) {

        String login = inputLogin(consoleManager, scanner);
        String password;
        String replyPassword;

        if (consoleManager.getConsole() != null){
            Passwords passwords = inputPassword(consoleManager);
            password = passwords.password;
            replyPassword = passwords.replyPassword;
            while (!password.equals(replyPassword)) {
                consoleManager.error("Пароли не совпадают");
                passwords = inputPassword(consoleManager);
                password = passwords.password;
                replyPassword = passwords.replyPassword;
            }
        }
        else {
            Passwords passwords = inputPassword(consoleManager, scanner);
            password = passwords.password;
            replyPassword = passwords.replyPassword;
            while (!password.equals(replyPassword)) {
                consoleManager.error("Пароли не совпадают");
                passwords = inputPassword(consoleManager, scanner);
                password = passwords.password;
                replyPassword = passwords.replyPassword;
            }
        }

        return createRequest(login, password);
    }
}
