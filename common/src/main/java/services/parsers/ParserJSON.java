package services.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import io.ConsoleManager;
import models.LabWork;
import request.Request;
import response.Response;
import services.model.ModelParse;
import services.parsers.interfaces.ParserElement;
import services.parsers.interfaces.ParserMap;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Класс для парсинга JSON файлов
 */

public class ParserJSON implements ParserElement, ParserMap<String, LabWork> {

    private final ObjectMapper mapper;

    public ParserJSON(){
        mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
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
    public <T> T deserializeElement(String json, Class<T> clazz) {
        try {
            TypeReference<T> typeRef = new TypeReference<>() {};
            return mapper.readValue(json, typeRef);
        } catch (IOException e) {
            return null;
        }
    }

    public LabWork deserializeLabWork(String json){
        try {
            TypeReference<LabWork> typeRef = new TypeReference<>() {};
            return mapper.readValue(json, typeRef);
        } catch (IOException e) {
            return null;
        }
    }

    public <T> T deserializeRequest(String json, Class<T> clazz) {
        try {
            TypeReference<Request> typeRef = new TypeReference<>() {};
            return mapper.readValue(json, typeRef);
        } catch (IOException e) {
            return null;
        }
    }

    public <T> T deserializeResponse(String json) {
        try {
            TypeReference<Response> typeRef = new TypeReference<>() {};
            return mapper.readValue(json, typeRef);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String serializeElement(Object elements) {
        String json = "";
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(elements);
        } catch (JsonProcessingException e) {
            return json;
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
            return null;
        }
    }

    @Override
    public String serializeMap(Map<String, LabWork> elements) {

        String json = "";
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(elements);
        } catch (JsonProcessingException e) {
            return json;
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
            return null;
        }
        return date;
    }

    public String serializeModelParse(ModelParse modelParse) throws JsonProcessingException {
        String res = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(modelParse);
        return res;
    }

}
