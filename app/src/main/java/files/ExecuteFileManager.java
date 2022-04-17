package files;

import files.file.FileManager;
import files.file.ReadCommand;
import files.file.ScriptWork;
import io.ConsoleManager;
import models.Coordinates;
import models.Difficulty;
import models.LabWork;
import models.Person;
import models.service.GenerationID;
import services.parsers.ParserJSON;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для запуска файла со скриптами
 */

public class ExecuteFileManager extends FileManager implements ReadCommand, ScriptWork {

    private ConsoleManager consoleManager;

    public ExecuteFileManager(String fileName, ConsoleManager consoleManager) {
        super(fileName);
        this.consoleManager = consoleManager;
    }

    @Override
    public Map getCommands() {
        return null;
    }

    @Override
    public List<String> readFile() {

        List<String> strings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(getFileName()))) {

            String s = "";

            while((s=reader.readLine())!=null) {
                strings.add(s);
            }

        } catch (IOException | NullPointerException e) {
            consoleManager.error("Во время работы программы возникла проблема с файлом");
        }
        return strings;
    }
}
