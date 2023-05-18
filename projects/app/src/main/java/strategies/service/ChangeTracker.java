package strategies.service;

import strategies.model.Character;
import strategies.model.Item;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ChangeTracker {
    private final List<Character> changedCharacters = new ArrayList<>();
    private final List<Item> changedItems = new ArrayList<>();

    //TODO: Track all changes for all objects here

    public List<Character> getChangedCharacters() {
        List<Character> changedCharactersCopy = new ArrayList<>(this.changedCharacters);
        changedCharacters.clear();
        return changedCharactersCopy;
    }

    public List<Item> getChangedItems() {
        List<Item> changedItemsCopy = new ArrayList<>(this.changedItems);
        changedItems.clear();
        return changedItemsCopy;
    }
}
