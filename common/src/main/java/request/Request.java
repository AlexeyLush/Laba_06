package request;

import services.parsers.ParserJSON;

public class Request {
    public final String commandName;
    public final Object element;

    public Request(String commandName, Object element) {
        this.commandName = commandName;
        this.element = new ParserJSON();
    }


}
