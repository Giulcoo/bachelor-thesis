package strategies.model;

public class Change<T> {
    private String id;

    enum Type{
        CHUNK,
        PLAYER
    }

    private Type type;

    enum Event{
        ADDED,
        REMOVED,
        UPDATED
    }

    private Event event;
    private String key;
    private T value;

    public String getId() {
        return id;
    }

    public Change<T> setId(String id) {
        this.id = id;
        return this;
    }

    public Type getType() {
        return type;
    }

    public Change<T> setType(Type type) {
        this.type = type;
        return this;
    }

    public Event getEvent() {
        return event;
    }

    public Change<T> setEvent(Event event) {
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
