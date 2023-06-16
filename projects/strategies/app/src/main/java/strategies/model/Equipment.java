package strategies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Equipment
{
    @JsonIgnore public static final String PROPERTY_NAME = "name";
    @JsonIgnore public static final String PROPERTY_TYPE = "type";
    @JsonIgnore public static final String PROPERTY_STRENGTH = "strength";
    @JsonIgnore public static final String PROPERTY_CHARACTER = "character";
    private String name;
    private String type;
    private double strength;
    @JsonIgnore private Character character;
    @JsonIgnore protected PropertyChangeSupport listeners;

    public String getName()
    {
        return this.name;
    }

    public Equipment setName(String value)
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

    public String getType()
    {
        return this.type;
    }

    public Equipment setType(String value)
    {
        if (Objects.equals(value, this.type))
        {
            return this;
        }

        final String oldValue = this.type;
        this.type = value;
        this.firePropertyChange(PROPERTY_TYPE, oldValue, value);
        return this;
    }

    public double getStrength()
    {
        return this.strength;
    }

    public Equipment setStrength(double value)
    {
        if (value == this.strength)
        {
            return this;
        }

        final double oldValue = this.strength;
        this.strength = value;
        this.firePropertyChange(PROPERTY_STRENGTH, oldValue, value);
        return this;
    }

    public Character getCharacter()
    {
        return this.character;
    }

    public Equipment setCharacter(Character value)
    {
        if (this.character == value)
        {
            return this;
        }

        final Character oldValue = this.character;
        if (this.character != null)
        {
            this.character = null;
            oldValue.withoutEquipment(this);
        }
        this.character = value;
        if (value != null)
        {
            value.withEquipment(this);
        }
        this.firePropertyChange(PROPERTY_CHARACTER, oldValue, value);
        return this;
    }

    public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
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
        return "Equip: " + this.name + " with strength " + this.strength;
    }

    public void removeYou()
    {
        this.setCharacter(null);
    }

    public Equipment copy(){
        return new Equipment()
            .setName(this.getName())
            .setType(this.getType())
            .setStrength(this.getStrength());
    }
}

