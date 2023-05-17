package strategies.model;

import java.util.List;

public class Equipment {
    private Character character;
    private String name;
    private String type;
    private double strength;

    public Character getCharacter() {
        return character;
    }

    public Equipment setCharacter(Character character) {
        if(this.character == character) return this;

        final Character oldCharacter = this.character;
        if(this.character != null){
            this.character = null;
            oldCharacter.withoutEquipment(this);
        }

        this.character = character;
        if(character != null) character.withEquipment(this);
        return this;
    }

    public String getName() {
        return name;
    }

    public Equipment setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public Equipment setType(String type) {
        this.type = type;
        return this;
    }

    public double getStrength() {
        return strength;
    }

    public Equipment setStrength(double strength) {
        this.strength = strength;
        return this;
    }

    @Override
    public String toString() {
        return "Equip: " + this.name + " with strength " + this.strength;
    }
}
