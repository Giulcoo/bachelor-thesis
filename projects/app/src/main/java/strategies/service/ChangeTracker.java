package strategies.service;

import strategies.model.Character;
import strategies.model.Chunk;
import strategies.model.Item;
import strategies.model.Obstacle;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ChangeTracker {
    /*private final List<Obstacle> addedObstacles = new ArrayList<>();
    private final List<Character> changedCharacters = new ArrayList<>();
    private final List<Item> changedItems = new ArrayList<>();
    private final List<Character> deletedCharacters = new ArrayList<>();
    private final List<Item> deletedItems = new ArrayList<>();*/

    private final List<Chunk<Character>> changedCharacterChunks = new ArrayList<>();
    private final List<Chunk<Character>> removedCharacterChunks = new ArrayList<>();
    private final List<Chunk<Item>> changedCItemChunks = new ArrayList<>();
    private final List<Chunk<Item>> removedItemChunks = new ArrayList<>();
    private final List<Chunk<Obstacle>> changedObstacleChunks = new ArrayList<>();

    public void addCharacterChange(Chunk<Character> chunk){
        if(!changedCharacterChunks.contains(chunk)) changedCharacterChunks.add(chunk);
    }

    public void addRemovedCharacterChunk(Chunk<Character> chunk){
        if(!removedCharacterChunks.contains(chunk)) removedCharacterChunks.add(chunk);
    }

    public void addItemChange(Chunk<Item> chunk){
        if(!changedCItemChunks.contains(chunk)) changedCItemChunks.add(chunk);
    }

    public void addRemovedItemChunk(Chunk<Item> chunk){
        if(!removedItemChunks.contains(chunk)) removedItemChunks.add(chunk);
    }

    public void addObstacleChange(Chunk<Obstacle> chunk){
        if(!changedObstacleChunks.contains(chunk)) changedObstacleChunks.add(chunk);
    }

    public List<Chunk<Character>> getCharacterChanges(){
        List<Chunk<Character>> changedCharacterChunksCopy = new ArrayList<>(this.changedCharacterChunks);
        this.changedCharacterChunks.clear();
        return changedCharacterChunksCopy;
    }

    public List<Chunk<Character>> getRemovedCharacterChunks(){
        List<Chunk<Character>> removedCharacterChunksCopy = new ArrayList<>(this.removedCharacterChunks);
        this.removedCharacterChunks.clear();
        return removedCharacterChunksCopy;
    }

    public List<Chunk<Item>> getItemChanges(){
        List<Chunk<Item>> changedItemChunksCopy = new ArrayList<>(this.changedCItemChunks);
        this.changedCItemChunks.clear();
        return changedItemChunksCopy;
    }

    public List<Chunk<Item>> getRemovedItemChunks(){
        List<Chunk<Item>> removedItemChunksCopy = new ArrayList<>(this.removedItemChunks);
        this.removedItemChunks.clear();
        return removedItemChunksCopy;
    }

    public List<Chunk<Obstacle>> getObstacleChange(){
        List<Chunk<Obstacle>> changedObstacleChunksCopy = new ArrayList<>(this.changedObstacleChunks);
        this.changedObstacleChunks.clear();
        return changedObstacleChunksCopy;
    }

    /*public void addChange(Obstacle obstacle){
        if(!addedObstacles.contains(obstacle)) addedObstacles.add(obstacle);
    }

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

    public List<Obstacle> getAddedObstacles() {
        List<Obstacle> addedObstaclesCopy = new ArrayList<>(this.addedObstacles);
        addedObstacles.clear();
        return addedObstaclesCopy;
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
    }*/
}
