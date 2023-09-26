package strategies.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import strategies.Constants;
import strategies.model.Change;
import strategies.model.Chunk;
import strategies.model.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChangeFileService {
    private final ObjectMapper mapper = new ObjectMapper();

    public ChangeFileService(){
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /** Change File: Add new Chunk */
    public void saveChunkAdded(Chunk chunk){
        saveChunkChange(chunk.getId(), Change.ADD_EVENT, null, chunk);
    }

    /** Change File: Update childChunks of chunk */
    public void saveChunkUpdate(String chunkID, List<String> childChunks){
        saveChunkChange(chunkID, Change.UPDATE_EVENT, "childChunks", childChunks);
    }

    /** Change File: Remove chunk */
    public void saveChunkRemoved(String chunkID){
        saveChunkChange(chunkID, Change.REMOVE_EVENT, null, null);
    }

    /** Change File: General chunk changes */
    public <T> void saveChunkChange(String chunkID, int event, String key, T value){
        saveChange(new Change<T>()
                .setId(chunkID)
                .setType(Change.CHUNK_TYPE)
                .setEvent(event)
                .setKey(key)
                .setValue(value));
    }


    /** Change File: Add new player */
    public void savePlayerAdded(Player player){
        savePlayerChange(player.getChunk(), player.getId(), Change.ADD_EVENT, null, player);
    }

    /** Change File: Update chunk of player */
    public void savePlayerUpdate(String playerID, String oldChunkID, String newChunkID){
        savePlayerChange(oldChunkID, playerID, Change.UPDATE_EVENT, "chunk", newChunkID);
    }

    /** Change File: Update position of player */
    public void savePlayerUpdate(Player player){
        savePlayerChange(player.getChunk(), player.getId(), Change.UPDATE_EVENT, "position", player.getPosition());
    }

    /** Change File: Remove player */
    public void savePlayerRemove(String playerID, String chunkID){
        savePlayerChange(chunkID, playerID, Change.REMOVE_EVENT, null, null);
    }

    /** Change File: General player changes */
    public <T> void savePlayerChange(String chunkID, String playerID, int event, String key, T value){
        saveChange(new Change<T>()
                .setId(chunkID + " " + playerID)
                .setType(Change.PLAYER_TYPE)
                .setEvent(event)
                .setKey(key)
                .setValue(value));
    }

    private <T> void saveChange(Change<T> change){
        FileManager.appendChanges(change);
    }
}
