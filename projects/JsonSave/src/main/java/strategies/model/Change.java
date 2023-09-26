package strategies.model;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Change<T> {
//    private final String ID_KEY = "id";
//    private final String TYPE_KEY = "type";
//    private final String EVENT_KEY = "event";
//    private final String KEY_KEY = "key";
//    private final String VALUE_KEY = "value";
    public static final int CHUNK_TYPE = 1;
    public static final int PLAYER_TYPE = 2;
    public static final int ADD_EVENT = 1;
    public static final int REMOVE_EVENT = 2;
    public static final int UPDATE_EVENT = 3;

    private String id;
    private int type;
    private int event;
    private String key;
    private T value;

    public String getId() {
        return id;
    }

    public Change<T> setId(String id) {
        this.id = id;
        return this;
    }

    public int getType() {
        return type;
    }

    public Change<T> setType(int type) {
        this.type = type;
        return this;
    }

    public int getEvent() {
        return event;
    }

    public Change<T> setEvent(int event) {
        this.event = event;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Change<T> setKey(String key) {
        this.key = key;
        return this;
    }

    public T getValue() {
        return value;
    }

    public Change<T> setValue(T value) {
        this.value = value;
        return this;
    }
}
