package dao;

import models.LabWork;
import models.service.GenerationID;
import models.service.LabWorkComparator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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

        LabWork labFromMap = null;
        String key = null;
        for (Map.Entry<String, LabWork> entry : labWorkList.entrySet()) {
            if (entry.getValue().getId().equals(id)){
                labFromMap = entry.getValue();
                labWork.setId(labFromMap.getId());
                labWork.setCreationDate(labFromMap.getCreationDate());
                key = entry.getKey();
            }
        }

        if (labFromMap != null){
            labWorkList.replace(key, labWork);
        }

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

        Map<String, LabWork> sortedMap = new LinkedHashMap<>();
        List<Map.Entry<String,LabWork>> entries = new ArrayList<>(map.entrySet());

        entries.sort(new LabWorkComparator());

        for(Map.Entry<String, LabWork> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
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
