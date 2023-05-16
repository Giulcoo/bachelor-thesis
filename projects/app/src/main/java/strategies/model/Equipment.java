package strategies.model;

import java.util.List;

public class Equipment {
    private String name;
    private String type;
    private double strength;

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
