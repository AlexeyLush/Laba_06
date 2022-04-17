package files;


import dao.LabWorkDAO;
import files.file.FileCreator;
import files.file.FileManager;
import files.file.FileWork;
import files.file.FileWorkMap;
import io.ConsoleManager;
import models.Coordinates;
import models.Difficulty;
import models.LabWork;
import models.Person;
import models.service.GenerationID;
import server.Server;
import services.checkers.LabWorkChecker;
import services.model.ModelParse;
import services.parsers.ParserJSON;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Класс для работы с данными файлов
 */

public class DataFileManager extends FileManager implements FileWorkMap<String, LabWork>, FileCreator, FileWork {

    private final ConsoleManager consoleManager;
    private final Scanner scanner;
    private final String tempFileName;
    private boolean isMainFile;

    public DataFileManager(String fileName, String tempFileName, ConsoleManager consoleManager, Scanner scanner) {
        super(fileName);
        this.consoleManager = consoleManager;
        this.scanner = scanner;
        this.tempFileName = tempFileName;


        boolean isMainFile = true;
        Path file = Path.of(fileName);

        try {
            if (!Files.isReadable(file)) {
                Files.createFile(file);
            }
        } catch (IOException e) {
            isMainFile = false;
        }

        this.isMainFile = isMainFile;
        if (!isMainFile) {
            consoleManager.warning("Программа не смогла получить доступ к основному файлу из-за ограничений. Программа будет работать с временным файлом");
        }
        initialFile(fileName);
    }


    public boolean isMainFile() {
        return isMainFile;
    }

    public void initialFile(String fileName) {

        File file = new File(fileName);
        File tempFile = new File(tempFileName);
        try {
            if (!isMainFile) {
                if (tempFile.createNewFile()) {
                    createFile();
                }
            } else {
                if (tempFile.canRead()) {
                    if (isMainFile) {
                        consoleManager.warning("Внимание! На вашем компьютере был обнаружен временный файл с данными");
                        if (file.createNewFile()) {
                            String tempData = readFile(tempFileName);
                            writeFile(fileName, tempData);
                            consoleManager.successfully("Данные из временного фалйа были перенесены в основной фалй!");
                            tempFile.delete();
                        } else {
                            consoleManager.output("Хотите ли вы перенести данные коллекции с временного файла в основной? (yes/no): ");
                            String response = scanner.nextLine();
                            boolean isTrueResponse = response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")
                                    || response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n");

                            while (!isTrueResponse) {
                                consoleManager.output("Хотите ли вы перенести данные коллекции с временного файла в основной? (yes/no): ");
                                response = scanner.nextLine();
                                isTrueResponse = response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")
                                        || response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n");
                            }

                            if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
                                Map<String, LabWork> tempCollection = readMap(tempFileName, false, false);
                                Map<String, LabWork> mainCollection = readMap(getFileName(), true, false);
                                if (tempCollection != null && mainCollection != null) {
                                    mainCollection.putAll(tempCollection);
                                    if (isCollection(mainCollection, false) != null) {
                                        save(mainCollection);
                                        consoleManager.successfully("Данные из временного фалйа были перенесены в основной фалй!");
                                    } else {
                                        consoleManager.error("Данные во времнном файле были повреждены, перенос данных невозможен");
                                    }
                                } else {
                                    consoleManager.error("Данные во времнном или главном файлах были повреждены, перенос данных невозможен");
                                }
                            }
                        }
                        tempFile.delete();
                    }
                }
            }

            if (file.createNewFile()) {
                createFile();
            }

        } catch (IOException ioException) {
            consoleManager.error("Во время работы программы возникла проблема с файлом");
        }

    }

    public Map<String, LabWork> isCollection(Map<String, LabWork> map, boolean isCreateFile) {

        LabWorkDAO labWorkDAO = new LabWorkDAO();
        try {
            LabWorkChecker labWorkChecker = new LabWorkChecker();
            int maxId = 0;

            List<Integer> listId = new ArrayList<>();
            for (Map.Entry<String, LabWork> entry : map.entrySet()) {
                if (maxId < entry.getValue().getId()) {
                    maxId = entry.getValue().getId();
                }
                String key = labWorkChecker.checkUserKey(entry.getKey(), labWorkDAO, consoleManager, true, false);
                Integer id = labWorkChecker.checkId(entry.getValue().getId().toString(), consoleManager, false);
                ZonedDateTime dateTime = labWorkChecker.checkDate(entry.getValue().getCreationDate().toString(), consoleManager, false);
                String name = labWorkChecker.checkNamePerson(entry.getValue().getName(), consoleManager, false);
                Long coordX = labWorkChecker.checkX(entry.getValue().getCoordinates().getX().toString(), consoleManager, false);
                Integer coordY = labWorkChecker.checkY(entry.getValue().getCoordinates().getY().toString(), consoleManager, false);
                Float minimalPoint = labWorkChecker.checkMinimalPoint(entry.getValue().getMinimalPoint().toString(), consoleManager, false);
                String description = labWorkChecker.checkDescription(entry.getValue().getDescription(), consoleManager, false);
                Difficulty difficulty = labWorkChecker.checkDifficulty(entry.getValue().getDifficulty().toString(), consoleManager, false);
                String authorName = labWorkChecker.checkNamePerson(entry.getValue().getAuthor().getName(), consoleManager, false);
                Long authorWeight = labWorkChecker.checkWeightPerson(entry.getValue().getAuthor().getWeight().toString(), consoleManager, false);
                String authorPassportId = labWorkChecker.checkPassportIdPerson(entry.getValue().getAuthor().getPassportID(), consoleManager, false);


                if (isCreateFile) {
                    if (listId.contains(id)) {
                        throw new IOException();
                    }
                } else {
                    if (listId.contains(id)) {
                        id = null;
                    }
                    while (id == null) {
                        id = labWorkChecker.checkId(GenerationID.newId().toString(), consoleManager, false);
                        if (listId.contains(id)) {
                            id = null;
                        }
                    }
                }

                if (key == null || id == null || dateTime == null || name == null || coordX == null || coordY == null
                        || minimalPoint == null || description == null || difficulty == null
                        || authorName == null || authorWeight == null || authorPassportId == null) {
                    throw new IOException();
                }
                listId.add(id);
                LabWork labWork = new LabWork(id, name, new Coordinates(coordX, coordY), dateTime, minimalPoint, description, difficulty,
                        new Person(authorName, authorWeight, authorPassportId));
                map.put(key, labWork);
            }

            GenerationID.setId(maxId + 1);

        } catch (IOException e) {
            return null;
        }

        return map;
    }

    @Override
    public Map<String, LabWork> readMap(String fileName, boolean isCreateFile, boolean withMessage) {

        Map<String, LabWork> labWorkMap = null;

        BufferedReader reader = null;
        try {
            ParserJSON parserJSON = new ParserJSON(consoleManager);
            reader = new BufferedReader(new FileReader(fileName));
            String s = "";
            String temp = "";

            while ((temp = reader.readLine()) != null) {
                s += temp;
            }

            if (ZonedDateTime.parse(parserJSON.getDataFromFile(s)) == null) {
                throw new IOException();
            }

            labWorkMap = parserJSON.deserializeMap(s);

            if (isCollection(labWorkMap, isCreateFile) == null) {
                throw new IOException();
            }


            if (withMessage) {
                if (isMainFile) {
                    consoleManager.successfully("Данные с основного файла успешно считаны!");
                } else {
                    consoleManager.successfully("Данные с временного файла успешно считаны!");
                }
            }

        } catch (IOException | NullPointerException | DateTimeException e) {
            if (isCreateFile) {
                consoleManager.error("Во время работы произошла ошибка");
                createFile();
                labWorkMap = readMap(fileName, isCreateFile, withMessage);
            }
        }

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                consoleManager.error("Во время работы произошла ошибка");
            }

        }

        return labWorkMap;
    }

    @Override
    public void save(Map<String, LabWork> labWorkMap) {
        String jsonWithDate = new ParserJSON(consoleManager).jsonForWrite(readFile(), labWorkMap);
        if (jsonWithDate != null) {
            try (Writer writer = new BufferedWriter(new FileWriter(getFileName()))) {
                writer.write(jsonWithDate);
            } catch (IOException e) {
                consoleManager.error("Во время работы программы возникла проблема с файлом");
            }
        } else {
            consoleManager.error("Во время работы программы возникла проблема с файлом");
        }

    }

    @Override
    public void createFile() {

        ModelParse modelParse = new ModelParse();
        Writer writer = null;
        try {
            if (isMainFile) {
                consoleManager.warning("Идёт создание файла...");
                writer = new BufferedWriter(new FileWriter(getFileName()));
            } else {
                consoleManager.warning("Идёт создание временного файла...");
                writer = new BufferedWriter(new FileWriter(tempFileName));
            }

            consoleManager.warning("Идёт запись значениями по умолчанию...");
            Map<String, LabWork> labWorkMap = new LinkedHashMap<>();
            LabWork labWork = new LabWork();
            modelParse.setDate(labWork.getCreationDate());
            labWork.setId(GenerationID.newId());

            labWork.setName("Лабораторная работа №1");

            Coordinates coordinates = new Coordinates();
            coordinates.setX(5L);
            coordinates.setY(3);

            labWork.setCoordinates(coordinates);
            labWork.setMinimalPoint(5f);
            labWork.setDescription("Это описание лабораторной работы №1");

            labWork.setDifficulty(Difficulty.NORMAL);

            Person author = new Person();
            author.setName("Alexey");
            author.setWeight(26L);
            author.setPassportID("336803");

            labWork.setAuthor(author);

            labWorkMap.put("1", labWork);


            modelParse.setCollection(labWorkMap);
            String json = new ParserJSON(consoleManager).serializeModelParse(modelParse);
            writer.write(json);

            writer.close();

            if (isMainFile) {
                consoleManager.successfully("Файл успешно создан!");
            } else {
                consoleManager.successfully("Временный файл успешно создан!");
            }

        } catch (IOException e) {
            consoleManager.error("Во время работы программы возникла проблема с файлом");
        }
        try {
            if (writer != null) {
                writer.close();
            }

        } catch (IOException e) {
            consoleManager.error("Во время работы программы возникла проблема с файлом");
        }

    }

    @Override
    public String readFile() {

        String s = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(getFileName()))) {

            String temp = "";

            while ((temp = reader.readLine()) != null) {
                s += temp;
            }

        } catch (IOException | NullPointerException e) {
            consoleManager.error("Во время работы программы возникла проблема с файлом");
        }

        return s;
    }

    @Override
    public String readFile(String fileName) {
        String s = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String temp = "";

            while ((temp = reader.readLine()) != null) {
                s += temp;
            }

        } catch (IOException | NullPointerException e) {
            consoleManager.error("Во время работы программы возникла проблема с файлом");
        }

        return s;
    }

    @Override
    public void writeFile(String fileName, String data) {

        try (Writer writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(data);
        } catch (IOException e) {
            consoleManager.error("Во время работы программы возникла проблема с файлом");
        }
    }
}
