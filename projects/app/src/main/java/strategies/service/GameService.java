package strategies.service;
import strategies.model.*;
import strategies.model.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static strategies.Constants.*;

public class GameService {
    private Game game;
    private Random random;
    private boolean verbose;

    public void createGame(int botCount, int obstacleCount, int itemCount, int seed, boolean verbose){
        random = new Random(seed);
        game = new Game();
        this.verbose = verbose;

        game.withCharacter(randomCharacter(false));
        for (int i = 0; i < botCount; i++) game.withCharacter(randomCharacter(true));

        for (int i = 0; i < obstacleCount; i++) game.withObstacle(randomObstacle());

        for (int i = 0; i < itemCount; i++) game.withItem(randomItem());
    }

    private Character randomCharacter(boolean isBot){
        return new Character()
                .setBot(isBot)
                .setName(isBot ? "Monster" : "Player")
                .setHp(100).setLvl(isBot? randInt(1,100) : 1)
                .setEquipment(randomEquipments());
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
        return new Item().setName("Coin")
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