package strategies.model;

import java.util.List;

public class Character {

    private Game game;
    private String name;
    private List<Equipment> equipment;
    private double hp;
    private int lvl;
    private boolean isBot;

    public Game getGame() {
        return game;
    }

    public Character setGame(Game game) {
        this.game = game;
        return this;
    }

    public String getName() {
        return name;
    }

    public Character setName(String name) {
        this.name = name;
        return this;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public Character setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
        return this;
    }

    public double getHp() {
        return hp;
    }

    public Character setHp(double hp) {
        this.hp = hp;
        return this;
    }

    public int getLvl() {
        return lvl;
    }

    public Character setLvl(int lvl) {
        this.lvl = lvl;
        return this;
    }

    public boolean isBot() {
        return isBot;
    }

    public Character setBot(boolean bot) {
        isBot = bot;
        return this;
    }
}
