package strategies.model;
import java.util.List;

public class Game {
    private List<Character> characters;
    private Character player;

    public Game setCharacters(List<Character> characters) {
        this.characters = characters;
        return this;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public Game setPlayer(Character player) {
        this.player = player;
        return this;
    }

    public Character getPlayer() {
        return player;
    }
}
