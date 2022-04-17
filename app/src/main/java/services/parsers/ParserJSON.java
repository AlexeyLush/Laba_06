package services.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import exception.ParserException;
import io.ConsoleManager;
import models.Coordinates;
import models.Difficulty;
import models.LabWork;
import models.Person;
import models.service.GenerationID;
import services.model.ModelParse;
import services.parsers.interfaces.ParserElement;
import services.parsers.interfaces.ParserMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Класс для парсинга JSON файлов
 */

public class ParserJSON implements ParserElement<LabWork>, ParserMap<String, LabWork> {

    private final ObjectMapper mapper;
    private final ConsoleManager consoleManager;

    public ParserJSON(ConsoleManager consoleManager){
        mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        this.consoleManager = consoleManager;
        mapper.registerModule(new JSR310Module());
        mapper.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        mapper.setDateFormat(df);
    }

    public boolean isDeserializeElement(String json){
        boolean isTrue = true;
        try {
            TypeReference<LabWork> typeRef = new TypeReference<LabWork>() {};
            mapper.readValue(json, typeRef);
        } catch (IOException e) {
            isTrue = false;
        }
        return isTrue;
    }

    @Override
    public LabWork deserializeElement(String json) {
        try {
            TypeReference<LabWork> typeRef = new TypeReference<LabWork>() {};
            return mapper.readValue(json, typeRef);
        } catch (IOException e) {
            consoleManager.error("Во время парсинга данных произошла ошибка");
        }
        return new LabWork();
    }

    @Override
    public String serializeElement(LabWork elements) {
        String json = "";
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(elements);
        } catch (JsonProcessingException e) {
            consoleManager.error("Во время парсинга данных произошла ошибка");
        }
        return json;
    }

    @Override
    public Map<String, LabWork> deserializeMap(String json) {
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode collection = root.get("collection");
            Map<String, LabWork> res = mapper.convertValue(collection, new TypeReference<LinkedHashMap<String, LabWork>>() {});
            return res;
        } catch (IOException | IllegalArgumentException exception) {
            consoleManager.error("Во время парсинга данных произошла ошибка");
            return null;
        }
    }

    @Override
    public String serializeMap(Map<String, LabWork> elements) {

        String json = "";
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(elements);
        } catch (JsonProcessingException e) {
            consoleManager.error("Во время парсинга данных произошла ошибка");
        }
        return json;
    }

    public String jsonForWrite(String file, Map<String, LabWork> map){
        ModelParse modelParse = new ModelParse();
        String res = "";
        try {
            JsonNode root = mapper.readTree(file);
            ZonedDateTime date = ZonedDateTime.parse(root.get("date").asText());
            modelParse.setDate(date);
            modelParse.setCollection(map);
            res = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(modelParse);
        } catch (IOException e) {
            consoleManager.error("Во время парсинга данных произошла ошибка");
            return null;
        }
        return res;
    }

    public String getDataFromFile(String file){
        String date = "";
        try {
            JsonNode root = mapper.readTree(file);
            date = root.get("date").asText();
        } catch (IOException e) {
            consoleManager.error("Во время парсинга данных произошла ошибка");
            return null;
        }
        return date;
    }

    public String serializeModelParse(ModelParse modelParse) throws JsonProcessingException {
        String res = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(modelParse);
        return res;
    }

}
