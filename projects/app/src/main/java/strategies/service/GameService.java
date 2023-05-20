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
    private Game game;
    private Random random;
    private boolean verbose;

    public GameService(ChangeTracker changeTracker) {
        this.changeTracker = changeTracker;
    }

    public void createGame(int botCount, int obstacleCount, int itemCount, int seed, boolean verbose){
        random = new Random(seed);
        game = new Game(changeTracker);
        this.verbose = verbose;

        game.withCharacters(randomCharacter(false));
        for (int i = 0; i < botCount; i++) game.withCharacters(randomCharacter(true));

        for (int i = 0; i < obstacleCount; i++) game.withObstacles(randomObstacle());

        for (int i = 0; i < itemCount; i++) game.withItems(randomItem());

        printGame();
    }

    public void randomChanges(int charMoves, int itemMoves, int charRemoveCount, int itemRemoveCount, int charAddCount, int itemAddCount){
        for (int i = 0; i < charMoves; i++) randomCharacterMove();
        for (int i = 0; i < itemMoves; i++) randomItemMove();

        for(int i = 0; i < charRemoveCount; i++) {
            if(game.getCharacters().size() > 1) game.withoutCharacters(game.getCharacters().get(randInt(1, game.getCharacters().size() - 1)));
        }

        for(int i = 0; i < itemRemoveCount; i++) {
            if(game.getItems().size() > 1) game.withoutItems(game.getItems().get(randInt(1, game.getItems().size() - 1)));
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
        game.getCharacters().get(randInt(0, game.getCharacters().size() - 1))
                .getPosition().add(new Vector(randDouble(-1, 1), randDouble(-1, 1), 0));
    }

    private void randomItemMove() {
        game.getItems().get(randInt(0, game.getItems().size() - 1))
                .getPosition().add(new Vector(randDouble(-1, 1), randDouble(-1, 1), 0));
    }

    private Character randomCharacter(boolean isBot){
        return new Character()
                .setIsBot(isBot)
                .setName(isBot ? "Monster" : "Player")
                .setId(UUID.randomUUID().toString())
                .setHp(100).setLvl(isBot? randInt(1,100) : 1)
                .setPosition(new Vector(randDouble(-100, 100), randDouble(-100, 100), 0))
                .setRotation(new Quaternion(0, 0, 0, 0))
                .withEquipment(randomEquipments());
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
        return new Obstacle().setName("Cube")
                .setPosition(new Vector(randDouble(-100, 100), randDouble(-100, 100), 0))
                .setScale(new Vector(randDouble(1, 10), randDouble(1, 10), randDouble(1, 10)))
                .setRotation(new Quaternion(randDouble(-1,1), randDouble(-1,1), randDouble(-1,1), randDouble(-1,1)));
    }

    private Item randomItem(){
        return new Item()
                .setName("Coin")
                .setId(UUID.randomUUID().toString())
                .setPosition(new Vector(randDouble(-100, 100), randDouble(-100, 100), 0))
                .setRotation(new Quaternion(randDouble(-1,1), randDouble(-1,1), randDouble(-1,1), randDouble(-1,1)));
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

    public void printGame(){
        if(verbose) System.out.println(game);
    }
}