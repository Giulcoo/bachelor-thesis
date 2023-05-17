package strategies.model;

public class Obstacle {
    public Game game;
    public String name;
    public Vector position;
    public Vector scale;
    public Quaternion rotation;

    public Game getGame() {
        return game;
    }

    public Obstacle setGame(Game game) {
        if(this.game == game) return this;

        Game oldGame = this.game;
        if(this.game != null){
            this.game = null;
            oldGame.withoutObstacle(this);
        }

        this.game = game;
        if(game != null){
            game.withObstacle(this);
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public Obstacle setName(String name) {
        this.name = name;
        return this;
    }

    public Vector getPosition() {
        return position;
    }

    public Obstacle setPosition(Vector position) {
        this.position = position;
        return this;
    }

    public Vector getScale() {
        return scale;
    }

    public Obstacle setScale(Vector scale) {
        this.scale = scale;
        return this;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public Obstacle setRotation(Quaternion rotation) {
        this.rotation = rotation;
        return this;
    }

    @Override
    public String toString() {
        return "Obst: " + name + " with pos: " + position + " scale:" + scale + " rotation:" + rotation;
    }
}
