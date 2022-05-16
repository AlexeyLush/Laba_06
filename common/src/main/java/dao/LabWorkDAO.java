package dao;

import models.LabWork;
import models.service.GenerationID;
import models.service.LabWorkComparator;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Класс для реализации работы с коллекцией LabWork
 */

public class LabWorkDAO implements DAO<String, LabWork>, MapWork<String, LabWork> {

    private Map<String, LabWork> labWorkList = new LinkedHashMap<>();

    @Override
    public int create(String key, LabWork labWork) {
        if (labWork.getId() == null || labWork.getId() == 0){
            labWork.setId(GenerationID.newId());
        }
        labWorkList.put(key, labWork);
        return labWork.getId();
    }

    @Override
    public void update(int id, LabWork labWork) {

        Optional<Map.Entry<String, LabWork>> labWorkOptional = labWorkList
                .entrySet()
                .stream()
                .filter(x -> x.getValue().getId() == id)
                .findFirst();
        labWorkOptional.ifPresent(entry -> labWorkList.replace(entry.getKey(), labWork));
    }

    @Override
    public void delete(String key) {
        LabWork labWork = get(key);
        if (labWork != null){
            labWorkList.remove(key);
        }
    }

    @Override
    public void initialMap(Map<String, LabWork> elements) {
        this.labWorkList = elements;
    }

    @Override
    public void clear() {
        labWorkList.clear();
    }

    @Override
    public Map<String, LabWork> sort(Map<String, LabWork> map) {

        return labWorkList
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    @Override
    public LabWork get(String key) {
        return labWorkList.get(key);
    }

    @Override
    public Map<String, LabWork> getAll() {
        return new LinkedHashMap<>(sort(labWorkList));
    }
}
