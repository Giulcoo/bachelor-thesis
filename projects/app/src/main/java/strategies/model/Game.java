package strategies.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;

public class Game
{
    public static final String PROPERTY_ITEMS = "items";
    public static final String PROPERTY_OBSTACLES = "obstacles";
    public static final String PROPERTY_CHARACTERS = "characters";
    private List<Item> items;
    private List<Obstacle> obstacles;
    private List<Character> characters;
    protected PropertyChangeSupport listeners;

    public List<Item> getItems()
    {
        return this.items != null ? Collections.unmodifiableList(this.items) : Collections.emptyList();
    }

    public Game withItems(Item value)
    {
        if (this.items == null)
        {
            this.items = new ArrayList<>();
        }
        if (!this.items.contains(value))
        {
            this.items.add(value);
            value.setGame(this);
            this.firePropertyChange(PROPERTY_ITEMS, null, value);
        }
        return this;
    }

    public Game withItems(Item... value)
    {
        for (final Item item : value)
        {
            this.withItems(item);
        }
        return this;
    }

    public Game withItems(Collection<? extends Item> value)
    {
        for (final Item item : value)
        {
            this.withItems(item);
        }
        return this;
    }

    public Game withoutItems(Item value)
    {
        if (this.items != null && this.items.remove(value))
        {
            value.setGame(null);
            this.firePropertyChange(PROPERTY_ITEMS, value, null);
        }
        return this;
    }

    public Game withoutItems(Item... value)
    {
        for (final Item item : value)
        {
            this.withoutItems(item);
        }
        return this;
    }

    public Game withoutItems(Collection<? extends Item> value)
    {
        for (final Item item : value)
        {
            this.withoutItems(item);
        }
        return this;
    }

    public List<Obstacle> getObstacles()
    {
        return this.obstacles != null ? Collections.unmodifiableList(this.obstacles) : Collections.emptyList();
    }

    public Game withObstacles(Obstacle value)
    {
        if (this.obstacles == null)
        {
            this.obstacles = new ArrayList<>();
        }
        if (!this.obstacles.contains(value))
        {
            this.obstacles.add(value);
            value.setGame(this);
            this.firePropertyChange(PROPERTY_OBSTACLES, null, value);
        }
        return this;
    }

    public Game withObstacles(Obstacle... value)
    {
        for (final Obstacle item : value)
        {
            this.withObstacles(item);
        }
        return this;
    }

    public Game withObstacles(Collection<? extends Obstacle> value)
    {
        for (final Obstacle item : value)
        {
            this.withObstacles(item);
        }
        return this;
    }

    public Game withoutObstacles(Obstacle value)
    {
        if (this.obstacles != null && this.obstacles.remove(value))
        {
            value.setGame(null);
            this.firePropertyChange(PROPERTY_OBSTACLES, value, null);
        }
        return this;
    }

    public Game withoutObstacles(Obstacle... value)
    {
        for (final Obstacle item : value)
        {
            this.withoutObstacles(item);
        }
        return this;
    }

    public Game withoutObstacles(Collection<? extends Obstacle> value)
    {
        for (final Obstacle item : value)
        {
            this.withoutObstacles(item);
        }
        return this;
    }

    public List<Character> getCharacters()
    {
        return this.characters != null ? Collections.unmodifiableList(this.characters) : Collections.emptyList();
    }

    public Game withCharacters(Character value)
    {
        if (this.characters == null)
        {
            this.characters = new ArrayList<>();
        }
        if (!this.characters.contains(value))
        {
            this.characters.add(value);
            value.setGame(this);
            this.firePropertyChange(PROPERTY_CHARACTERS, null, value);
        }
        return this;
    }

    public Game withCharacters(Character... value)
    {
        for (final Character item : value)
        {
            this.withCharacters(item);
        }
        return this;
    }

    public Game withCharacters(Collection<? extends Character> value)
    {
        for (final Character item : value)
        {
            this.withCharacters(item);
        }
        return this;
    }

    public Game withoutCharacters(Character value)
    {
        if (this.characters != null && this.characters.remove(value))
        {
            value.setGame(null);
            this.firePropertyChange(PROPERTY_CHARACTERS, value, null);
        }
        return this;
    }

    public Game withoutCharacters(Character... value)
    {
        for (final Character item : value)
        {
            this.withoutCharacters(item);
        }
        return this;
    }

    public Game withoutCharacters(Collection<? extends Character> value)
    {
        for (final Character item : value)
        {
            this.withoutCharacters(item);
        }
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

    public void removeYou()
    {
        this.withoutItems(new ArrayList<>(this.getItems()));
        this.withoutObstacles(new ArrayList<>(this.getObstacles()));
        this.withoutCharacters(new ArrayList<>(this.getCharacters()));
    }

    @Override
    public String toString() {
        String result = "";

        for(Character character : characters) result += character.toString();
        for(Obstacle obstacle : obstacles) result += obstacle.toString() + "\n";
        for(Item item : items) result += item.toString() + "\n";

        return result;
    }
}