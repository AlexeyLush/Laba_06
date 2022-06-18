package response;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Response {

    public int statusCode;
    public ZonedDateTime creationDate;
    public Type contentType;
    public Object argument;
    public String message;
    public String command;

    public Response(){
        creationDate = ZonedDateTime.now();
        message = "";
    }

    public Response(int statusCode, Type contentType, Object argument, String message, String command) {
        this.statusCode = statusCode;
        this.creationDate = ZonedDateTime.now();
        this.contentType = contentType;
        this.argument = argument;
        this.message = message;
        this.command = command;
    }


    public enum Type {
        INPUT("INPUT"),
        LIST("LIST"),
        TEXT("TEXT"),
        INSERT("INSERT"),
        INPUT_SCRIPT("INPUT_SCRIPT"),
        UPDATE("UPDATE");

        private String type;

        Type(String type){
            this.type = type;
        }

        public String getType(){
            return this.type;
        }
    }

    private String getVersion() {
        return "HTTP/1.1";
    }
    private String getDate() {
        return "Date: " + creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }
    private String getStatusCode(){
        String result = "";
        switch (statusCode){
            case 200 : {
                result = statusCode + " OK";
                break;
            }
            case 201 : {
                result = statusCode + " Created";
                break;
            }
            case 400: {
                result = statusCode + " Bad Request";
                break;
            }
            case 404: {
                result = statusCode + " Not Found";
                break;
            }
            case 500: {
                result = statusCode + " Internal Server Error";
                break;
            }
            case 409 : {
                result = statusCode + " Conflict";
                break;
            }
        }
        return result;
    }
    private String getServerName() { return "Server: Local Machine";}
    private String getContentType() {
        return "Content-Type: " + contentType.getType();
    }
    private String getContentLength() {
        return "Content-Length: " + argument.toString().length();
    }
    private String getMessage() {
        if (message != null && !message.equals("") && !message.isEmpty())
            return "Message: " + message;
        else
            return "";
    }
    private String getArgument() {
        return "Argument: " + argument;
    }

    @Override
    public String toString() {
        return  getVersion() + System.lineSeparator() +
                getDate() + System.lineSeparator() +
                getStatusCode() + System.lineSeparator() +
                getServerName() + System.lineSeparator() +
                getContentType() + System.lineSeparator() +
                getContentLength() + System.lineSeparator() +
                getMessage() + System.lineSeparator();
    }
}
