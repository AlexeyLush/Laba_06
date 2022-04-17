package exception;

import io.ConsoleManager;

public abstract class CustomException extends Exception {

    private String message;
    private final ConsoleManager consoleManager;

    public CustomException() {
        this.consoleManager = new ConsoleManager();
    }

    protected void setMessage(String message){
        this.message = message;
    }

    public void outputException(ConsoleManager manager) {
        outputException();
    }

    public void outputException(){
        consoleManager.error(message);
    }

}
