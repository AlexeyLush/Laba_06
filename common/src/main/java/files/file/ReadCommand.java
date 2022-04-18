package files.file;

import java.util.Map;

/**
 * Интерфейс для чтения команд
 */

public interface ReadCommand<K,V> {
    Map<K,V> getCommands();
}
