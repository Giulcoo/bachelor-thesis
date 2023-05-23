package strategies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Item
{
    @JsonIgnore public static final String PROPERTY_NAME = "name";
    @JsonIgnore public static final String PROPERTY_ID = "id";
    @JsonIgnore public static final String PROPERTY_POSITION = "position";
    @JsonIgnore public static final String PROPERTY_ROTATION = "rotation";
    @JsonIgnore public static final String PROPERTY_GAME = "game";
    private String name;
    private String id;
    private Vector position;
    private Quaternion rotation;
    @JsonIgnore private Game game;
    @JsonIgnore protected PropertyChangeSupport listeners;

    public String getName()
    {
        return this.name;
    }

    public Item setName(String value)
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

    public Item setId(String value)
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

    public Item setPosition(Vector value)
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

    public Item setRotation(Quaternion value)
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

    public Game getGame()
    {
        return this.game;
    }

    public Item setGame(Game value)
    {
        if (this.game == value)
        {
            return this;
        }

        final Game oldValue = this.game;
        if (this.game != null)
        {
            this.game = null;
            oldValue.withoutItems(this);
        }
        this.game = value;
        if (value != null)
        {
            value.withItems(this);
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
        return "Item: #" + this.id + " " + name + " with pos: " + position + " rotation:" + rotation;
    }

    public void removeYou()
    {
        this.setGame(null);
    }

    public Item copy()
    {
        return new Item()
            .setId(this.getId())
            .setName(new String(this.getName()))
            .setPosition(this.getPosition().copy())
            .setRotation(this.getRotation().copy());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Item){
            return ((Item) obj).getId().equals(this.getId());
        }

        return false;
    }
}