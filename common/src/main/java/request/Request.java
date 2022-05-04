package request;

import models.LabWork;
import services.parsers.ParserJSON;

public class Request {

    public String commandName;
    public Object element;

    public Request(){

    }

    public Request(String commandName, Object element) {
        this.commandName = commandName;
        this.element = new ParserJSON().serializeElement(element);
    }

    public <T> T getArgumentAs(Class<T> clazz) {
        return new ParserJSON().deserializeElement((String) element, clazz);
    }


}
