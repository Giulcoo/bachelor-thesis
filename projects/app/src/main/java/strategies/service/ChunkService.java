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
        characterChunks.add(new Chunk<>(new Vector(0, 0, 0), new Vector(Constants.MAP_SIZE, Constants.MAP_SIZE, 0)));
        itemChunks.add(new Chunk<>(new Vector(0, 0, 0), new Vector(Constants.MAP_SIZE, Constants.MAP_SIZE, 0)));
        obstacleChunks.add(new Chunk<>(new Vector(0, 0, 0), new Vector(Constants.MAP_SIZE, Constants.MAP_SIZE, 0)));
    }

    public void addElement(Character element){
        for(int i = 0; i < characterChunks.size(); i++){
            Chunk<Character> chunk = characterChunks.get(i);
            if(chunk.pointInChunk(element)){
                chunk.addElement(element);
                changeTracker.addCharacterChange(chunk);

                List<Chunk<Character>> newChunks = chunk.checkChunkSize();
                newChunks.forEach(changeTracker::addCharacterChange);
                characterChunks.addAll(newChunks);
                break;
            }
        }
    }

    public void addElement(Item element){
        for(int i = 0; i < itemChunks.size(); i++){
            Chunk<Item> chunk = itemChunks.get(i);
            if(chunk.pointInChunk(element)){
                chunk.addElement(element);
                changeTracker.addItemChange(chunk);

                List<Chunk<Item>> newChunks = chunk.checkChunkSize();
                newChunks.forEach(changeTracker::addItemChange);
                itemChunks.addAll(newChunks);
                break;
            }
        }
    }

    public void addElement(Obstacle element){
        for(int i = 0; i < obstacleChunks.size(); i++){
            Chunk<Obstacle> chunk = obstacleChunks.get(i);
            if(chunk.pointInChunk(element)){
                chunk.addElement(element);
                changeTracker.addObstacleChange(chunk);

                List<Chunk<Obstacle>> newChunks = chunk.checkChunkSize();
                newChunks.forEach(changeTracker::addObstacleChange);
                obstacleChunks.addAll(newChunks);
                break;
            }
        }
    }

    public void removeElement(Character element){
        changeTracker.addCharacterChange(element.getChunk());
        element.getChunk().removeElement(element);
    }

    public void removeElement(Item element){
        changeTracker.addItemChange(element.getChunk());
        element.getChunk().removeElement(element);
    }

    public void updateElement(Character element){
        Chunk<Character> currentChunk = element.getChunk();
        if(currentChunk.pointInChunk(element)) return;

        for(Chunk<Character> c : currentChunk.getNeighbours()){
            if(c.pointInChunk(element)){
                changeTracker.addCharacterChange(currentChunk);
                currentChunk.removeElement(element);

                c.addElement(element);
                changeTracker.addCharacterChange(c);

                List<Chunk<Character>> newChunks = c.checkChunkSize();
                newChunks.forEach(changeTracker::addCharacterChange);
                characterChunks.addAll(newChunks);
                return;
            }
        }
    }
    public void updateElement(Item element){
        Chunk<Item> currentChunk = element.getChunk();
        if(currentChunk.pointInChunk(element)) return;

        for(Chunk<Item> c : currentChunk.getNeighbours()){
            if(c.pointInChunk(element)){
                changeTracker.addItemChange(currentChunk);
                currentChunk.removeElement(element);

                c.addElement(element);
                changeTracker.addItemChange(c);

                List<Chunk<Item>> newChunks = c.checkChunkSize();
                newChunks.forEach(changeTracker::addItemChange);
                itemChunks.addAll(newChunks);
                return;
            }
        }
    }

    public void printChunks(){
        System.out.println("Character chunks:");
        int index = 0;
        for(Chunk<Character> c : characterChunks){
            System.out.println("Chunk " + index + ":");
            System.out.println(c);
            System.out.println();
            index++;
        }

        System.out.println("Item chunks:");
        index = 0;
        for(Chunk<Item> c : itemChunks){
            System.out.println("Chunk " + index + ":");
            System.out.println(c);
            System.out.println();
            index++;
        }

        System.out.println("Obstacle chunks:");
        index = 0;
        for(Chunk<Obstacle> c : obstacleChunks){
            System.out.println("Chunk " + index + ":");
            System.out.println(c);
            System.out.println();
            index++;
        }
    }
}
