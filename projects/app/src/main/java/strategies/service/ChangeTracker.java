package strategies.service;

import strategies.model.Character;
import strategies.model.Item;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ChangeTracker {
    private final List<Character> changedCharacters = new ArrayList<>();
    private final List<Item> changedItems = new ArrayList<>();
    private final List<Character> deletedCharacters = new ArrayList<>();
    private final List<Item> deletedItems = new ArrayList<>();

    //TODO: Track all changes for all objects here

    public void addChange(Character character){
        if(!changedCharacters.contains(character)) changedCharacters.add(character);
    }

    public void addChange(Item item){
        if(!changedItems.contains(item)) changedItems.add(item);
    }

    public void addRemovedObject(Character character){
        if(!deletedCharacters.contains(character)) deletedCharacters.add(character);
    }

    public void addRemovedObject(Item item){
        if(!deletedItems.contains(item)) deletedItems.add(item);
    }

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

    public List<Character> getDeletedCharacters() {
        List<Character> deletedCharactersCopy = new ArrayList<>(this.deletedCharacters);
        deletedCharacters.clear();
        return deletedCharactersCopy;
    }

    public List<Item> getDeletedItems() {
        List<Item> deletedItemsCopy = new ArrayList<>(this.deletedItems);
        deletedItems.clear();
        return deletedItemsCopy;
    }
}
