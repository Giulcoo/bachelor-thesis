package strategies.model;

public class Character {
    private Equipment equipment;
    private double hp;
    private double lvl;
    private boolean isBot;

    public Character setEquipment(Equipment equipment){
        this.equipment = equipment;
        return this;
    }

    public Equipment getEquipment(){
        return this.equipment;
    }

    public Character setHp(double hp){
        this.hp = hp;
        return this;
    }

    public double getHp(){
        return this.hp;
    }

    public Character setLvl(double lvl){
        this.lvl = lvl;
        return this;
    }

    public double getLvl(){
        return this.lvl;
    }

    public Character setIsBot(boolean isBot){
        this.isBot = isBot;
        return this;
    }

    public boolean getIsBot(){
        return this.isBot;
    }
}
