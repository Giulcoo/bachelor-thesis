package strategies.model;

import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Item
{
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_POSITION = "position";
    public static final String PROPERTY_ROTATION = "rotation";
    public static final String PROPERTY_GAME = "game";
    private String name;
    private Vector position;
    private Quaternion rotation;
    private Game game;
    protected PropertyChangeSupport listeners;

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
        return "Item: " + name + " with pos: " + position + " rotation:" + rotation;
    }

    public void removeYou()
    {
        this.setGame(null);
    }
}