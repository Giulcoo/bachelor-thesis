package strategies.model;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Character> characters;
    private List<Obstacle> obstacles;
    private List<Item> items;

    public Game setCharacters(List<Character> characters) {
        this.characters = characters;
        characters.forEach(c -> c.setGame(this));
        return this;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public Game withCharacter(Character character) {
        if(this.characters == null){
            this.characters = new ArrayList<>();
        }
        if(!this.characters.contains(character)){
            this.characters.add(character);
            character.setGame(this);
        }
        return this;
    }

    public Game withoutCharacter(Character character) {
        if(this.characters != null && this.characters.remove(character)){
            character.setGame(null);
        }
        return this;
    }

    public Game setObstacles(List<Obstacle> obstacles) {
        this.obstacles = obstacles;
        obstacles.forEach(c -> c.setGame(this));
        return this;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public Game withObstacle(Obstacle obstacle) {
        if(this.obstacles == null){
            this.obstacles = new ArrayList<>();
        }
        if(!this.obstacles.contains(obstacle)){
            this.obstacles.add(obstacle);
            obstacle.setGame(this);
        }
        return this;
    }

    public Game withoutObstacle(Obstacle obstacle) {
        if(this.obstacles != null && this.obstacles.remove(obstacle)){
            obstacle.setGame(null);
        }
        return this;
    }

    public Game setItems(List<Item> items) {
        this.items = items;
        items.forEach(c -> c.setGame(this));
        return this;
    }

    public List<Item> getItems() {
        return items;
    }

    public Game withItem(Item item) {
        if(this.items == null){
            this.items = new ArrayList<>();
        }
        if(!this.items.contains(item)){
            this.items.add(item);
            item.setGame(this);
        }
        return this;
    }

    public Game withoutItem(Item item) {
        if(this.items != null && this.items.remove(item)){
            item.setGame(null);
        }
        return this;
    }

    @Override
    public String toString() {
        String result = "";

        for(Character character : characters) result += character.toString();
        for(Obstacle obstacle : obstacles) result += obstacle.toString() + "\n";
        for(Item item : items) result += item.toString() + "\n";

        return result;
    }
}
