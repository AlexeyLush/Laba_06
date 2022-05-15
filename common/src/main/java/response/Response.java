package response;

import services.parsers.ParserJSON;

public class Response {

    public Status status;
    public Type type;
    public Object argument;
    public String message;
    public String command;
    public boolean isWait;

    public Response(){

    }
    public Response(Status status, Type type, Object argument) {
        this.status = status;
        this.type = type;
        this.argument = argument;
        message = null;
    }

    public enum Type {
        INPUT,
        LIST,
        TEXT,
        INSERT,
        INPUT_SCRIPT,
        UPDATE
    }

    public enum Status {
        OK,
        ERROR
    }

}
