package strategies.service;

import strategies.Constants;
import strategies.model.Change;
import strategies.model.Chunk;
import strategies.model.GameInfo;

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

        //TODO
    }

    private void removeChunk(String chunkID){
        FileManager.deleteFile(chunkID);
    }

    public Chunk getChunk(String chunkID){
        //TODO
        return null;
    }

    public void saveGameInfo(GameInfo info){
        //TODO
    }

    public GameInfo loadGameInfo(){
        //TODO
        return null;
    }

    public void applyChanges(List<Change> changes){
        //TODO
    }

    private void applyChunkChange(Change change){
        //TODO
    }

    private void applyPlayerChange(Change change){
        //TODO
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

    /** Close all active FileStreams */
    public void close(){
       //TODO Maybe not needed
    }
}
