package strategies.service;

import strategies.model.Character;
import strategies.model.Chunk;
import strategies.model.Item;
import strategies.model.Obstacle;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ChangeTracker {
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
}
