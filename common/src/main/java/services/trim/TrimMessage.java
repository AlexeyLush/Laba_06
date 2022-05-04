package services.trim;

public class TrimMessage {

    public String trimZero(String message){
        int pos = message.indexOf(0);
        return pos == -1 ? message : message.substring(0, pos);
    }

}
