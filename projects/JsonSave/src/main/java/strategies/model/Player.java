package strategies.model;

public class Player {
    private String id;
    private String name;
    private boolean isBot;
    private String chunk;
    private Vector position;

    public String getId() {
        return id;
    }

    public Player setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public boolean getIsBot() {
        return isBot;
    }

    public Player setIsBot(boolean bot) {
        isBot = bot;
        return this;
    }

    public String getChunk() {
        return chunk;
    }

    public Player setChunk(String chunk) {
        this.chunk = chunk;
        return this;
    }

    public Vector getPosition() {
        return position;
    }

    public Player setPosition(Vector position) {
        this.position = position;
        return this;
    }
}
