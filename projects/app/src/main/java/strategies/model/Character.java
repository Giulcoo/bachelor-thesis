package strategies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;

public class Character
{
    @JsonIgnore public static final String PROPERTY_NAME = "name";
    @JsonIgnore public static final String PROPERTY_ID = "id";
    @JsonIgnore public static final String PROPERTY_POSITION = "position";
    @JsonIgnore public static final String PROPERTY_ROTATION = "rotation";
    @JsonIgnore public static final String PROPERTY_HP = "hp";
    @JsonIgnore public static final String PROPERTY_EXP = "exp";
    @JsonIgnore public static final String PROPERTY_LVL = "lvl";
    @JsonIgnore public static final String PROPERTY_IS_BOT = "isBot";
    @JsonIgnore public static final String PROPERTY_EQUIPMENT = "equipment";
    @JsonIgnore public static final String PROPERTY_GAME = "game";
    private String name;
    private String id;
    private Vector position;
    private Quaternion rotation;
    private double hp;
    private double exp;
    private int lvl;
    private boolean isBot;
    private List<Equipment> equipment;
    @JsonIgnore private Game game;
    @JsonIgnore protected PropertyChangeSupport listeners;

    public String getName()
    {
        return this.name;
    }

    public Character setName(String value)
    {
        if (Objects.equals(value, this.name))
        {
            return this;
        }

        final String oldValue = this.name;
        this.name = value;
        this.firePropertyChange(PROPERTY_NAME, oldValue, value);
        return this;
    }

    public String getId()
    {
        return this.id;
    }

    public Character setId(String value)
    {
        if (Objects.equals(value, this.id))
        {
            return this;
        }

        final String oldValue = this.id;
        this.id = value;
        this.firePropertyChange(PROPERTY_ID, oldValue, value);
        return this;
    }


    public Vector getPosition()
    {
        return this.position;
    }

    public Character setPosition(Vector value)
    {
        if (Objects.equals(value, this.position))
        {
            return this;
        }

        final Vector oldValue = this.position;
        this.position = value;
        this.firePropertyChange(PROPERTY_POSITION, oldValue, value);
        return this;
    }

    public Quaternion getRotation()
    {
        return this.rotation;
    }

    public Character setRotation(Quaternion value)
    {
        if (Objects.equals(value, this.rotation))
        {
            return this;
        }

        final Quaternion oldValue = this.rotation;
        this.rotation = value;
        this.firePropertyChange(PROPERTY_ROTATION, oldValue, value);
        return this;
    }

    public double getHp()
    {
        return this.hp;
    }

    public Character setHp(double value)
    {
        if (value == this.hp)
        {
            return this;
        }

        final double oldValue = this.hp;
        this.hp = value;
        this.firePropertyChange(PROPERTY_HP, oldValue, value);
        return this;
    }

    public double getExp()
    {
        return this.exp;
    }

    public Character setExp(double value)
    {
        if (value == this.exp)
        {
            return this;
        }

        final double oldValue = this.exp;
        this.exp = value;
        this.firePropertyChange(PROPERTY_EXP, oldValue, value);
        return this;
    }

    public int getLvl()
    {
        return this.lvl;
    }

    public Character setLvl(int value)
    {
        if (value == this.lvl)
        {
            return this;
        }

        final int oldValue = this.lvl;
        this.lvl = value;
        this.firePropertyChange(PROPERTY_LVL, oldValue, value);
        return this;
    }

    public boolean isIsBot()
    {
        return this.isBot;
    }

    public Character setIsBot(boolean value)
    {
        if (value == this.isBot)
        {
            return this;
        }

        final boolean oldValue = this.isBot;
        this.isBot = value;
        this.firePropertyChange(PROPERTY_IS_BOT, oldValue, value);
        return this;
    }

    public List<Equipment> getEquipment()
    {
        return this.equipment != null ? Collections.unmodifiableList(this.equipment) : Collections.emptyList();
    }

    public Character withEquipment(Equipment value)
    {
        if (this.equipment == null)
        {
            this.equipment = new ArrayList<>();
        }
        if (!this.equipment.contains(value))
        {
            this.equipment.add(value);
            value.setCharacter(this);
            this.firePropertyChange(PROPERTY_EQUIPMENT, null, value);
        }
        return this;
    }

    public Character withEquipment(Equipment... value)
    {
        for (final Equipment item : value)
        {
            this.withEquipment(item);
        }
        return this;
    }

    public Character withEquipment(Collection<? extends Equipment> value)
    {
        for (final Equipment item : value)
        {
            this.withEquipment(item);
        }
        return this;
    }

    public Character withoutEquipment(Equipment value)
    {
        if (this.equipment != null && this.equipment.remove(value))
        {
            value.setCharacter(null);
            this.firePropertyChange(PROPERTY_EQUIPMENT, value, null);
        }
        return this;
    }

    public Character withoutEquipment(Equipment... value)
    {
        for (final Equipment item : value)
        {
            this.withoutEquipment(item);
        }
        return this;
    }

    public Character withoutEquipment(Collection<? extends Equipment> value)
    {
        for (final Equipment item : value)
        {
            this.withoutEquipment(item);
        }
        return this;
    }

    public Game getGame()
    {
        return this.game;
    }

    public Character setGame(Game value)
    {
        if (this.game == value)
        {
            return this;
        }

        final Game oldValue = this.game;
        if (this.game != null)
        {
            this.game = null;
            oldValue.withoutCharacters(this);
        }
        this.game = value;
        if (value != null)
        {
            value.withCharacters(this);
        }
        this.firePropertyChange(PROPERTY_GAME, oldValue, value);
        return this;
    }

    public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        if(this.game != null) this.game.getChangeTracker().addChange(this);

        if (this.listeners != null)
        {
            this.listeners.firePropertyChange(propertyName, oldValue, newValue);
            return true;
        }
        return false;
    }

    public PropertyChangeSupport listeners()
    {
        if (this.listeners == null)
        {
            this.listeners = new PropertyChangeSupport(this);
        }
        return this.listeners;
    }

    @Override
    public String toString()
    {
        String equipment = "";
        for(Equipment e : this.equipment) equipment += "   " + e.toString() + "\n";

        return "Char: #" + this.id + " " + this.name + " with hp: " + this.hp + " exp: " + exp +  " lvl: " + this.lvl + " position: " + position + " equipment:\n" + equipment;
    }

    public void removeYou()
    {
        this.withoutEquipment(new ArrayList<>(this.getEquipment()));
        this.setGame(null);
    }

    public Character copy(){
        Character copy = new Character()
                .setId(this.getId())
                .setName(new String(this.getName()))
                .setHp(this.getHp())
                .setExp(this.getExp())
                .setLvl(this.getLvl())
                .setPosition(this.getPosition().copy())
                .setRotation(this.getRotation().copy())
                .setIsBot(this.isIsBot());

        this.getEquipment().forEach(e -> copy.withEquipment(e.copy()));
        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Character){
            return ((Character) obj).getId().equals(this.getId());
        }

        return false;
    }
}