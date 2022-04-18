package services.model;

import models.LabWork;

import java.time.ZonedDateTime;
import java.util.Map;

public class ModelParse {
    private ZonedDateTime date;
    private Map<String, LabWork> collection;

    public String getDate() {
        return date.toString();
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Map<String, LabWork> getCollection() {
        return collection;
    }

    public void setCollection(Map<String, LabWork> collection) {
        this.collection = collection;
    }
}
