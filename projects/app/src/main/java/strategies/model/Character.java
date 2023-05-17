package strategies.model;

import java.util.ArrayList;
import java.util.List;

public class Character {
    private Game game;
    private String name;
    private List<Equipment> equipment;
    private Vector position;
    private Quaternion rotation;
    private double hp;
    private int lvl;
    private boolean isBot;

    public Game getGame() {
        return game;
    }

    public Character setGame(Game game) {
        if(this.game == game) return this;

        final Game oldGame = this.game;
        if (this.game != null)
        {
            this.game = null;
            oldGame.withoutCharacter(this);
        }

        this.game = game;
        if (game != null) game.withCharacter(this);
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
        equipment.forEach(e -> e.setCharacter(this));
        return this;
    }

    public Character withEquipment(Equipment equipment) {
        if(this.equipment == null) this.equipment = new ArrayList<>();

        if(!this.equipment.contains(equipment)) {
            this.equipment.add(equipment);
            equipment.setCharacter(this);
        }
        return this;
    }

    public Character withoutEquipment(Equipment equipment) {
        if(this.equipment != null && this.equipment.remove(equipment)){
            equipment.setCharacter(null);
        }

        return this;
    }

    public Vector getPosition() {
        return position;
    }

    public Character setPosition(Vector position) {
        this.position = position;
        return this;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public Character setRotation(Quaternion rotation) {
        this.rotation = rotation;
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

    @Override
    public String toString() {
        String equipment = "";
        for(Equipment e : this.equipment) equipment += "   " + e.toString() + "\n";

        return "Char: " + this.name + " with hp: " + this.hp + " and lvl: " + this.lvl + " and equipment:\n" + equipment;
    }
}
