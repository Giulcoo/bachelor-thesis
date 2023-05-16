package strategies.model;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Character> characters = new ArrayList<>();

    public Game setCharacters(List<Character> characters) {
        this.characters = characters;
        return this;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    @Override
    public String toString() {
        String result = "";

        for(Character character : characters) result += character.toString() + "\n";

        return result;
    }
}
