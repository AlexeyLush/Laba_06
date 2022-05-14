package response;

import services.parsers.ParserJSON;

public class Response {

    public Status status;
    public Type type;
    public Object argument;
    public String message;

    public Response(){

    }
    public Response(Status status, Type type, Object argument) {
        this.status = status;
        this.type = type;
        this.argument = argument;
        message = null;
    }

    public enum Type {
        LIST,
        TEXT,
        INSERT,
        INPUT,
        UPDATE
    }

    public enum Status {
        OK,
        ERROR
    }

}
