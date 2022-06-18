package request;

public class Request {

    public String authorization;
    public String method;
    public String host;
    public String contentType;
    public int contentLength;
    public String message;
    public Object element;

    public Request(){}

    public Request(String authorization, String method, String host, String contentType, int contentLength, String message, Object element) {
        this.authorization = authorization;
        this.method = method;
        this.host = host;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.message = message;
        this.element = element;
    }

    private String getAuthorization() { return "Authorization: Basic " + authorization; }

    private String getMethod() { return  "Method: " + method; }

    private String getHost() {
        return "Host:" + host;
    }

    private String getContentType() {
        return "Content-Type: " + contentType;
    }

    private String getContentLength() {
        return "Content-Length: " + contentLength;
    }

    private String getMessage(){
        return "Message: " + message;
    }

    private Object getElement() { return "Element: " + element; }

    @Override
    public String toString() {
        return  getAuthorization() + System.lineSeparator() +
                getMethod() + System.lineSeparator() +
                getHost() + System.lineSeparator() +
                getContentType() + System.lineSeparator() +
                getContentLength() + System.lineSeparator() +
                getMessage() + System.lineSeparator() +
                getElement() + System.lineSeparator();
    }
}
