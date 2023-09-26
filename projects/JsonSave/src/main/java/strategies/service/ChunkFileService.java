package strategies.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import strategies.Constants;
import strategies.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkFileService {
    private final Map<String, Chunk>  changedChunks = new HashMap<>();
    private final List<String> removedChunkIDs = new ArrayList<>();

    /** Chunk Saving: Add changed chunk */
    public void addChangedChunk(Chunk chunk){
        changedChunks.put(chunk.getId(), chunk);
    }

    public void addChangedChunks(List<Chunk> chunks){
        chunks.forEach(this::addChangedChunk);
    }

    public void addRemovedChunk(String chunkID){
        if(!removedChunkIDs.contains(chunkID)){
            removedChunkIDs.add(chunkID);
        }
    }

    /** Chunk Saving: Add removed chunk */
    public void addRemovedChunk(Chunk chunk){
        addRemovedChunk(chunk.getId());
    }

    /** Chunk Saving: Add multiple removed chunks */
    public void addRemovedChunks(List<String> chunks){
        chunks.forEach(this::addRemovedChunk);
    }

    /** Chunk Saving: Save every changed chunk and delete files of removed chunks */
    public void saveChanges(){
        createChunkFolderIfNeeded();
        changedChunks.values().forEach(this::saveChunk);
        removedChunkIDs.forEach(this::removeChunk);

        changedChunks.clear();
        removedChunkIDs.clear();
    }

    private void createChunkFolderIfNeeded(){
        try{
            if(!Files.exists(Constants.CHUNK_FOLDER)) Files.createDirectory(Constants.CHUNK_FOLDER);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void saveChunk(Chunk chunk){
        String chunkPath = Constants.CHUNK_PATH + chunk.getId();
        FileManager.writeToFile(chunkPath, chunk);
    }

    private void removeChunk(String chunkID){
        FileManager.deleteFile(chunkID);
    }

    public Chunk getChunk(String chunkID){
        return FileManager.readFile(Constants.CHUNK_PATH + chunkID + ".json", Chunk.class);
    }

    public void saveGameInfo(GameInfo info){
        FileManager.writeToFile(Constants.INFO_FILE, info);
    }

    public GameInfo loadGameInfo(){
        return FileManager.readFile(Constants.INFO_FILE, GameInfo.class);
    }

    public void applyChanges(){
        List<JsonNode> changes = FileManager.readChanges();

        changes.forEach(change -> {
            if(change.get("type").asInt() == Change.CHUNK_TYPE){
                applyChunkChange(change);
            }
            else{
                applyPlayerChange(change);
            }
        });

        changedChunks.values().forEach(this::saveChunk);
        changedChunks.clear();
    }

    private void applyChunkChange(JsonNode change){
        String chunkID = change.get("id").asText();

        switch(change.get("event").asInt()){
            case Change.ADD_EVENT -> {
                Chunk chunk = nodeToChunk(change.get("value"));
                if(chunk != null) changedChunks.put(chunk.getId(), chunk);
            }
            case Change.REMOVE_EVENT -> removeChunk(chunkID);
            case Change.UPDATE_EVENT -> {
                Chunk chunk = getOrLoadChunk(chunkID);

                List<String> oldChildren = chunk.getChildChunksList();

                if(!oldChildren.isEmpty()) oldChildren.forEach(this::removeChunk);

                chunk.clearChildChunks();

                chunk.addAllChildChunks(nodeToStringList(change.get("value")));
                changedChunks.put(chunkID, chunk);
            }
        }
    }

    private void applyPlayerChange(JsonNode change){
        String[] ids = change.get("id").asText().split("\\s+");
        String chunkID = ids[0];
        String playerID = ids[1];
        Chunk chunk = getOrLoadChunk(chunkID);

        switch(change.get("event").asInt()){
            case Change.ADD_EVENT -> {
                Player player = nodeToPlayer(change.get("value"));
                chunk.addPlayer(player);
            }
            case Change.REMOVE_EVENT -> chunk.removePlayer(chunk.indexOfPlayer(playerID));
            case Change.UPDATE_EVENT -> {
                String key = change.get("key").asText();
                JsonNode value = change.get("value");
                Player player = chunk.getPlayer(playerID);

                if(key.equals("chunk")){
                    String newChunkID = value.asText();
                    Chunk newChunk = getOrLoadChunk(newChunkID);

                    //Add player to new chunk
                    player.setChunk(newChunkID);
                    newChunk.addPlayer(player);
                    //Remove player from old chunk
                    chunk.removePlayer(chunk.indexOfPlayer(playerID));
                }
                else if(key.equals("position")){
                    player.setPosition(nodeToVector(value));
                }
            }
        }
    }

    private Chunk getOrLoadChunk(String chunkID){
        if(changedChunks.containsKey(chunkID)){
            return changedChunks.get(chunkID);
        }
        else{
            Chunk chunk = getChunk(chunkID);
            changedChunks.put(chunkID, chunk);
            return chunk;
        }
    }

    private Chunk nodeToChunk(JsonNode node){
        try {
            return new ObjectMapper().treeToValue(node, Chunk.class);
        }
        catch (JsonProcessingException e){
            System.out.println("Could not get chunk");
            return null;
        }
    }

    private Player nodeToPlayer(JsonNode node){
        try {
            return new ObjectMapper().treeToValue(node, Player.class);
        }
        catch (JsonProcessingException e){
            System.out.println("Could not get chunk");
            return null;
        }
    }

    private Vector nodeToVector(JsonNode node){
        try {
            return new ObjectMapper().treeToValue(node, Vector.class);
        }
        catch (JsonProcessingException e){
            System.out.println("Could not get chunk");
            return null;
        }
    }

    private List<String> nodeToStringList(JsonNode node){
        List<String> values = new ArrayList<>();
        node.get("value").forEach(n -> values.add(n.asText()));
        return values;
    }
}
