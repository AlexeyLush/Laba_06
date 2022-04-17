package models.service;

import models.LabWork;
import models.Person;

import java.util.Comparator;
import java.util.Map;

public class LabWorkComparator implements Comparator<Map.Entry<String, LabWork>> {
    @Override
    public int compare(Map.Entry<String, LabWork> a, Map.Entry<String, LabWork> b) {
        return a.getValue().compareTo(b.getValue());
    }
}
