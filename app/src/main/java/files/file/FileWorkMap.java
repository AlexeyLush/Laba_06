package files.file;

import java.io.IOException;
import java.util.Map;

/**
 * Интерфейс для работы с файлом
 */

public interface FileWorkMap<K, V> {

    Map<K, V> readMap(String fileName, boolean isCreateFile, boolean withMessage) throws IOException;
    void save(Map<K, V> elements);

}
