package GameState;

import java.io.Serializable;

public interface ISaveManager {
    Object load(String fileName) throws Exception;
    void save(Serializable data, String fileName) throws Exception;
}
