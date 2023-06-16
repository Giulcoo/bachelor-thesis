package strategies.service;
import strategies.model.*;
import strategies.model.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static strategies.Constants.*;

public class GameService {
    private final ChangeTracker changeTracker;
    private final ChunkService chunkService;
    private Game game;
    private Random random;
    private boolean verbose;

    public GameService(ChangeTracker changeTracker, ChunkService chunkService) {
        this.changeTracker = changeTracker;
        this.chunkService = chunkService;
    }

    public Game getGame(){
        return game;
    }

    public void createGame(int obstacleCount, int botCount, int itemCount, int seed, boolean verbose){
        random = new Random(seed);
        game = new Game(changeTracker);
        this.verbose = verbose;

        game.withCharacters(randomCharacter(false));
        for (int i = 0; i < botCount; i++) game.withCharacters(randomCharacter(true));

        for (int i = 0; i < obstacleCount; i++) game.withObstacles(randomObstacle());

        for (int i = 0; i < itemCount; i++) game.withItems(randomItem());

        printGame();
    }

    public void createGame(int seed, boolean verbose){
        random = new Random(seed);
        game = new Game(changeTracker);
        this.verbose = verbose;
    }

    public void addElement(Character character){
        game.withCharacters(character);
    }

    public void addElement(Item item){
        game.withItems(item);
    }

    public void addElement(Obstacle obstacle){
        game.withObstacles(obstacle);
    }

    public void addCharacters(List<Character> characters){
        game.withCharacters(characters);
    }

    public void addItems(List<Item> items){
        game.withItems(items);
    }

    public void addObstacles(List<Obstacle> obstacles){
        game.withObstacles(obstacles);
    }

    public void randomChanges(int charMoves, int itemMoves, int charRemoveCount, int itemRemoveCount, int charAddCount, int itemAddCount){
        for (int i = 0; i < charMoves; i++) randomCharacterMove();
        for (int i = 0; i < itemMoves; i++) randomItemMove();

        for(int i = 0; i < charRemoveCount; i++) {
            if(game.getCharacters().size() > 1) {
                Character randChar = game.getCharacters().get(randInt(1, game.getCharacters().size() - 1));
                chunkService.removeElement(randChar);
                game.withoutCharacters(randChar);
            }
        }

        for(int i = 0; i < itemRemoveCount; i++) {
            if(game.getItems().size() > 1){
                Item randItem = game.getItems().get(randInt(1, game.getItems().size() - 1));
                chunkService.removeElement(randItem);
                game.withoutItems(randItem);
            }
        }

        for(int i = 0; i < charAddCount; i++) {
            game.withCharacters(randomCharacter(true));
        }

        for(int i = 0; i < itemAddCount; i++) {
            game.withItems(randomItem());
        }

        printGame();
    }

    private void randomCharacterMove() {
        Character randChar = game.getCharacters().get(randInt(0, game.getCharacters().size() - 1));
        randChar.setPosition(checkPosition(randChar.getPosition().add(new Vector(randDouble(-1, 1), randDouble(-1, 1), 0))));

        chunkService.updateElement(randChar);
    }

    private void randomItemMove() {
        Item randItem = game.getItems().get(randInt(0, game.getItems().size() - 1));
        randItem.setPosition(checkPosition(randItem.getPosition().add(new Vector(randDouble(-1, 1), randDouble(-1, 1), 0))));

        chunkService.updateElement(randItem);
    }

    private Character randomCharacter(boolean isBot){
        Character newChar = new Character()
                .setIsBot(isBot)
                .setName(isBot ? "Monster" : "Player")
                .setId(UUID.randomUUID().toString())
                .setHp(100).setLvl(isBot? randInt(1,100) : 1)
                .setPosition(new Vector(randDouble(-MAP_SIZE/2, MAP_SIZE/2), randDouble(-MAP_SIZE/2, MAP_SIZE/2), 0))
                .setRotation(new Quaternion(0, 0, 0, 0))
                .withEquipment(randomEquipments());

        chunkService.addElement(newChar);
        return newChar;
    }

    private List<Equipment> randomEquipments(){
        ArrayList<Equipment> equipments = new ArrayList<>();

        //Add weapon
        equipments.add(randomEquipment(WEAPONS, WEAPON_TYPE));

        //Add armor
        if(randBool(.7)) equipments.add(randomEquipment(HEAD_ARMORS, HEAD_ARMOR_TYPE));
        if(randBool(.8)) equipments.add(randomEquipment(BODY_ARMORS, BODY_ARMOR_TYPE));
        if(randBool(.6)) equipments.add(randomEquipment(LEG_ARMORS, LEG_ARMOR_TYPE));
        if(randBool(.8)) equipments.add(randomEquipment(FOOT_ARMORS, FOOT_ARMOR_TYPE));

        return equipments;
    }

    private Equipment randomEquipment(String[] names, String type){
        return new Equipment().setName(names[randInt(0, names.length - 1)])
                .setStrength(randInt(1, 10))
                .setType(type);
    }

    private Obstacle randomObstacle(){
        Obstacle newObst = new Obstacle().setName("Cube")
                .setPosition(new Vector(randDouble(-MAP_SIZE/2, MAP_SIZE/2), randDouble(-MAP_SIZE/2, MAP_SIZE/2), 0))
                .setScale(new Vector(randDouble(1, 10), randDouble(1, 10), randDouble(1, 10)))
                .setRotation(new Quaternion(randDouble(-1,1), randDouble(-1,1), randDouble(-1,1), randDouble(-1,1)));

        chunkService.addElement(newObst);
        return newObst;
    }

    private Item randomItem(){
        Item newItem = new Item()
                .setName("Coin")
                .setId(UUID.randomUUID().toString())
                .setPosition(new Vector(randDouble(-MAP_SIZE/2, MAP_SIZE/2), randDouble(-MAP_SIZE/2, MAP_SIZE/2), 0))
                .setRotation(new Quaternion(randDouble(-1,1), randDouble(-1,1), randDouble(-1,1), randDouble(-1,1)));

        chunkService.addElement(newItem);
        return newItem;
    }

    private double randDouble(double min, double max){
        return random.nextDouble() * (max - min) + min;
    }

    private int randInt(int min, int max){
        return random.nextInt(max - min + 1) + min;
    }

    private boolean randBool(double chance){
        return random.nextDouble() <= chance;
    }

    private Vector checkPosition(Vector position){
        if(position.getX() < -MAP_SIZE/2) position.setX(-MAP_SIZE/2);
        if(position.getX() > MAP_SIZE/2) position.setX(MAP_SIZE/2);
        if(position.getY() < -MAP_SIZE/2) position.setY(-MAP_SIZE/2);
        if(position.getY() > MAP_SIZE/2) position.setY(MAP_SIZE/2);
        return position;
    }

    public void printGame(){
        if(verbose) {
            //System.out.println(game);
            System.out.println("--------------------");
            chunkService.printChunks();
        }
    }
}