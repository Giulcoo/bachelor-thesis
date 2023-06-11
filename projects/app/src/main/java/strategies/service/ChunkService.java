package strategies.service;

import strategies.Constants;
import strategies.model.*;
import strategies.model.Character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChunkService {
    private final ChangeTracker changeTracker;
    private final List<Chunk<Character>> characterChunks = new ArrayList<>();
    private final List<Chunk<Item>> itemChunks = new ArrayList<>();
    private final List<Chunk<Obstacle>> obstacleChunks = new ArrayList<>();

    public ChunkService(ChangeTracker changeTracker) {
        this.changeTracker = changeTracker;
        characterChunks.add(new Chunk<>(null, new Vector(0, 0, 0), new Vector(Constants.MAP_SIZE, Constants.MAP_SIZE, 0)));
        itemChunks.add(new Chunk<>(null, new Vector(0, 0, 0), new Vector(Constants.MAP_SIZE, Constants.MAP_SIZE, 0)));
        obstacleChunks.add(new Chunk<>(null, new Vector(0, 0, 0), new Vector(Constants.MAP_SIZE, Constants.MAP_SIZE, 0)));
    }

    public void addElement(Character element){
        /*for(int i = 0; i < characterChunks.size(); i++){
            Chunk<Character> chunk = characterChunks.get(i);
            if(chunk.pointInChunk(element)){
                chunk.addElement(element);
                changeTracker.addCharacterChange(chunk);

                int sizeCheckResult = chunk.checkChunkSize();
                if(sizeCheckResult == 1){
                    chunk.getChildChunks().forEach(changeTracker::addCharacterChange);
                    characterChunks.addAll(chunk.getChildChunks());
                }
                else if(sizeCheckResult == -1){
                    //TODO: changeTracker removedChunk

                }

                break;
            }
        }*/

        Chunk<Character> chunk = characterChunks.get(0).findChunk(element);
        chunk.addElement(element);

        int sizeCheckResult = chunk.checkChunkSize();
        if(sizeCheckResult == 1){
            chunk.getChildChunks().forEach(changeTracker::addCharacterChange);
            characterChunks.addAll(chunk.getChildChunks());
        }
        else if(sizeCheckResult == -1){
            chunk = element.getChunk();

            chunk.getChildChunks().forEach(c -> {
                characterChunks.remove(c);
                changeTracker.addRemovedCharacterChunk(c);
            });

            chunk.clearChildren();
        }

        changeTracker.addCharacterChange(chunk);
    }

    public void addElement(Item element){
        Chunk<Item> chunk = itemChunks.get(0).findChunk(element);
        chunk.addElement(element);

        int sizeCheckResult = chunk.checkChunkSize();
        if(sizeCheckResult == 1){
            chunk.getChildChunks().forEach(changeTracker::addItemChange);
            itemChunks.addAll(chunk.getChildChunks());
        }
        else if(sizeCheckResult == -1){
            chunk = element.getChunk();

            chunk.getChildChunks().forEach(c -> {
                itemChunks.remove(c);
                changeTracker.addRemovedItemChunk(c);
            });

            chunk.clearChildren();
        }

        changeTracker.addItemChange(chunk);
    }

    public void addElement(Obstacle element){
        Chunk<Obstacle> chunk = obstacleChunks.get(0).findChunk(element);
        chunk.addElement(element);

        int sizeCheckResult = chunk.checkChunkSize();
        if(sizeCheckResult == 1){
            chunk.getChildChunks().forEach(changeTracker::addObstacleChange);
            obstacleChunks.addAll(chunk.getChildChunks());
        }

        changeTracker.addObstacleChange(chunk);
    }

    public void removeElement(Character element){
        Chunk<Character> chunk = element.getChunk();
        chunk.removeElement(element);

        if(chunk.checkChunkSize() == -1){
            chunk = chunk.getParent();

            chunk.getChildChunks().forEach(c -> {
                characterChunks.remove(c);
                changeTracker.addRemovedCharacterChunk(c);
            });

            chunk.clearChildren();
        }

        changeTracker.addCharacterChange(chunk);
    }

    public void removeElement(Item element){
        Chunk<Item> chunk = element.getChunk();
        chunk.removeElement(element);

        if(chunk.checkChunkSize() == -1){
            chunk = chunk.getParent();

            chunk.getChildChunks().forEach(c -> {
                itemChunks.remove(c);
                changeTracker.addRemovedItemChunk(c);
            });

            chunk.clearChildren();
        }

        changeTracker.addItemChange(chunk);
    }

    public void updateElement(Character element){
        Chunk<Character> oldChunk = element.getChunk();
        Chunk<Character> chunk = oldChunk.findChunk(element);

        changeTracker.addCharacterChange(oldChunk);
        if(oldChunk != chunk) changeTracker.addCharacterChange(chunk);
    }
    public void updateElement(Item element){
        Chunk<Item> oldChunk = element.getChunk();
        Chunk<Item> chunk = oldChunk.findChunk(element);

        changeTracker.addItemChange(oldChunk);
        if(oldChunk != chunk) changeTracker.addItemChange(chunk);
    }

    public void printChunks(){
       /* System.out.println("Character chunks:");*/
        int index = 0;
       /* for(Chunk<Character> c : characterChunks){
            System.out.println("Chunk " + index + ":");
            System.out.println(c);
            System.out.println();
            index++;
        }*/

        System.out.println("Item chunks:");
        index = 0;
        for(Chunk<Item> c : itemChunks){
            System.out.println("Chunk " + index + ":");
            System.out.println(c);
            System.out.println();
            index++;
        }

        /*System.out.println("Obstacle chunks:");
        index = 0;
        for(Chunk<Obstacle> c : obstacleChunks){
            System.out.println("Chunk " + index + ":");
            System.out.println(c);
            System.out.println();
            index++;
        }*/
    }
}
