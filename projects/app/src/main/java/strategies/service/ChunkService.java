package strategies.service;

import strategies.Constants;
import strategies.model.*;
import strategies.model.Character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChunkService {
    //Saves index of chunk from category
    private final HashMap<String, Integer> characterMap = new HashMap<>();
    private final HashMap<String, Integer> itemMap = new HashMap<>();

    //Saves all chunks from categories
    private final List<Chunk<Character>> characterChunks = new ArrayList<>();
    private final List<Chunk<Item>> itemChunks = new ArrayList<>();
    private final List<Chunk<Obstacle>> obstacleChunks = new ArrayList<>();

    //TODO: Start with one chunk, if chunk is to big, split it into two chunks with two groups of objects who are near each other

    public ChunkService() {
        characterChunks.add(new Chunk<>(new Vector(0, 0, 0), new Vector(Constants.MAP_SIZE, Constants.MAP_SIZE, 0)));
        itemChunks.add(new Chunk<>(new Vector(0, 0, 0), new Vector(Constants.MAP_SIZE, Constants.MAP_SIZE, 0)));
        obstacleChunks.add(new Chunk<>(new Vector(0, 0, 0), new Vector(Constants.MAP_SIZE, Constants.MAP_SIZE, 0)));
    }

    public void addElement(Character element){
        for(int i = 0; i < characterChunks.size(); i++){
            Chunk<Character> chunk = characterChunks.get(i);
            if(chunk.pointInChunk(element)){
                chunk.getElements().add(element);
                characterChunks.addAll(chunk.checkChunkSize());
                characterMap.put(element.getId(), i);
                break;
            }
        }
    }

    public void addElement(Item element){
        for(int i = 0; i < itemChunks.size(); i++){
            Chunk<Item> chunk = itemChunks.get(i);
            if(chunk.pointInChunk(element)){
                chunk.getElements().add(element);
                itemChunks.addAll(chunk.checkChunkSize());
                itemMap.put(element.getId(), i);
                break;
            }
        }
    }

    public void addElement(Obstacle element){
        for(int i = 0; i < obstacleChunks.size(); i++){
            Chunk<Obstacle> chunk = obstacleChunks.get(i);
            if(chunk.pointInChunk(element)){
                chunk.getElements().add(element);
                obstacleChunks.addAll(chunk.checkChunkSize());
                break;
            }
        }
    }
}
