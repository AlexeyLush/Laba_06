package services.spliters;

import io.ConsoleManager;
import services.parsers.ParserJSON;
import services.spliters.interfaces.SplitCommand;

public class SplitCommandOnIdAndJSON implements SplitCommand{

    @Override
    public String[] splitedCommand(String command, ConsoleManager consoleManager) {

        String[] splitCommand = command.split(" ");

        String key = null;
        String json = null;

        splitCommand[0] = "";
        if (splitCommand.length == 2){
            json = String.join(" ", splitCommand);
            if (!(new ParserJSON().isDeserializeElement(json))){
                key = splitCommand[1];
                json = null;
            }

        } else if (splitCommand.length > 2) {
            json = String.join(" ", splitCommand);

            if (!(new ParserJSON().isDeserializeElement(json))){
                key = splitCommand[1];
                splitCommand[1] = "";
                json = String.join(" ", splitCommand);
                if (!(new ParserJSON().isDeserializeElement(json))){
                    json = null;
                    key += String.join(" ", splitCommand);
                }
            }
        }

        return new String[] {key, json};
    }

}
