package strategies.service;

import strategies.Constants;
import strategies.model.Chunk;
import strategies.model.GameInfo;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkFileService {
    private final Map<String, Chunk.Builder>  changedChunks = new HashMap<>();
    private final List<String> removedChunkIDs = new ArrayList<>();

    private FileOutputStream gameInfoOutput;
    private FileInputStream gameInfoInput;

    private final Map<String, FileOutputStream> chunkOutputs = new HashMap<>();
    private final Map<String, FileInputStream> chunkInputs = new HashMap<>();

    /** Chunk Saving: Add changed chunk */
    public void addChangedChunk(Chunk.Builder chunk){
        if(changedChunks.containsKey(chunk.getId())) changedChunks.remove(chunk.getId());

        changedChunks.put(chunk.getId(), chunk);
    }

    public void addChangedChunks(List<Chunk.Builder> chunks){
        chunks.forEach(this::addChangedChunk);
    }

    public void addRemovedChunk(String chunkID){
        if(!removedChunkIDs.contains(chunkID)){
            removedChunkIDs.add(chunkID);
        }
    }

    /** Chunk Saving: Add removed chunk */
    public void addRemovedChunk(Chunk.Builder chunk){
        addRemovedChunk(chunk.getId());
    }

    /** Chunk Saving: Add multiple removed chunks */
    public void addRemovedChunks(List<Chunk.Builder> chunks){
        chunks.forEach(this::addRemovedChunk);
    }

    /** Chunk Saving: Save every changed chunk and delete files of removed chunks */
    public void saveChanges(){
        createChunkFolderIfNeeded();
        changedChunks.entrySet().stream().map(e -> e.getValue()).forEach(this::saveChunk);
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

    private void saveChunk(Chunk.Builder chunk){
        String chunkPath = Constants.CHUNK_PATH + chunk.getId();

        if(chunkOutputs.containsKey(chunk.getId())){
            FileManager.tryClose(chunkOutputs.get(chunk.getId()));
            chunkOutputs.remove(chunk.getId());
        }

        FileManager.clearFile(chunkPath);

        try{
            if(!chunkOutputs.containsKey(chunk.getId())) chunkOutputs.put(chunk.getId(), new FileOutputStream(chunkPath));

            chunk.build().writeTo(chunkOutputs.get(chunk.getId()));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void removeChunk(String chunkID){
        FileManager.deleteFile(chunkID);
    }

    public Chunk getChunk(String chunkID){
        try{
            if(!chunkInputs.containsKey(chunkID)){
                File chunkFile = new File(Constants.CHUNK_PATH + chunkID);
                chunkFile.createNewFile();
                chunkInputs.put(chunkID, new FileInputStream(chunkFile));
            }

            return Chunk.parseFrom(chunkInputs.get(chunkID));
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public void saveGameInfo(GameInfo info){
        FileManager.tryClose(this.gameInfoOutput);
        this.gameInfoOutput = null;
        FileManager.clearFile(Constants.INFO_FILE);

        try{
            if(this.gameInfoOutput == null) this.gameInfoOutput = new FileOutputStream(Constants.INFO_FILE);

            info.writeTo(this.gameInfoOutput);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public GameInfo loadGameInfo(){
        try{
            if(this.gameInfoInput == null) this.gameInfoInput = new FileInputStream(Constants.INFO_FILE);

            return GameInfo.parseFrom(this.gameInfoInput);
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /** Close all active FileStreams */
    public void close(){
        FileManager.tryClose(gameInfoOutput);
        FileManager.tryClose(gameInfoInput);
        chunkOutputs.entrySet().forEach(e -> FileManager.tryClose(e.getValue()));
        chunkInputs.entrySet().forEach(e -> FileManager.tryClose(e.getValue()));
    }
}
