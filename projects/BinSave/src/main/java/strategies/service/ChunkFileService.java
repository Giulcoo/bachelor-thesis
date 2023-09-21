package strategies.service;

import strategies.Constants;
import strategies.model.Chunk;
import strategies.model.GameInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkFileService {
//    private final List<String> changedChunkIDs = new ArrayList<>();
//    private final List<Chunk.Builder> changedChunks = new ArrayList<>();
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
        try{
            if(!chunkOutputs.containsKey(chunk.getId())) {
                File chunkFile = new File(Constants.CHUNK_PATH + chunk.getId());
                chunkFile.createNewFile();
                chunkOutputs.put(chunk.getId(), new FileOutputStream(chunkFile));
            }

            System.out.println("\n======================");
            System.out.println("Save Chunk");
            System.out.println("ID: " + chunk.getId() + " Center: (" + chunk.getPosition().getX() + ", " + chunk.getPosition().getY() + ")");
            System.out.println("Children:" + chunk.getPlayersList());

            chunk.build().writeTo(chunkOutputs.get(chunk.getId()));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void removeChunk(String chunkID){
        File chunkFile = new File(Constants.CHUNK_PATH + chunkID);
        chunkFile.delete();
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
        tryClose(gameInfoOutput);
        tryClose(gameInfoInput);
        chunkOutputs.entrySet().forEach(e -> tryClose(e.getValue()));
        chunkInputs.entrySet().forEach(e -> tryClose(e.getValue()));
    }

    private void tryClose(Closeable closeable){
        try {
            if (closeable != null) closeable.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
