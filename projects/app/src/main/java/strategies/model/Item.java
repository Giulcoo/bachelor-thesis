package strategies.model;

public class Item {
    public Game game;
    public String name;
    public Vector position;
    public Quaternion rotation;

    public Game getGame() {
        return game;
    }

    public Item setGame(Game game) {
        if(this.game == game) return this;

        Game oldGame = this.game;
        if(this.game != null){
            this.game = null;
            oldGame.withoutItem(this);
        }

        this.game = game;
        if(game != null){
            game.withItem(this);
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public Vector getPosition() {
        return position;
    }

    public Item setPosition(Vector position) {
        this.position = position;
        return this;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public Item setRotation(Quaternion rotation) {
        this.rotation = rotation;
        return this;
    }

    @Override
    public String toString() {
        return "Item: " + name + " with pos: " + position + " rotation:" + rotation;
    }
}
