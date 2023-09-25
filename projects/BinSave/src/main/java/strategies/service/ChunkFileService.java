package strategies.service;

import com.google.protobuf.InvalidProtocolBufferException;
import strategies.Constants;
import strategies.model.*;
import strategies.model.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class ChunkFileService {
    private final Map<String, Chunk.Builder>  changedChunks = new HashMap<>();
    private final List<String> removedChunkIDs = new ArrayList<>();

    private FileOutputStream gameInfoOutput;
    private FileInputStream gameInfoInput;

    private final Map<String, FileOutputStream> chunkOutputs = new HashMap<>();
    private final Map<String, FileInputStream> chunkInputs = new HashMap<>();

    /** Chunk Saving: Add changed chunk */
    public void addChangedChunk(Chunk.Builder chunk){
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

    private void saveChunk(Chunk.Builder chunk){
        String chunkPath = Constants.CHUNK_PATH + chunk.getId();
        FileManager.clearFile(chunkPath);

        try{
            chunk.build().writeTo(new FileOutputStream(chunkPath));
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
            Chunk chunk = Chunk.parseFrom(new FileInputStream(Constants.CHUNK_PATH + chunkID));

            if(chunk == null) throw new NullPointerException();

            return chunk;
        }
        catch (IOException e){
            System.out.println("ChunkFileService.getChunk(" + chunkID + ") does not find the Chunk");
            return null;
        }
        catch(NullPointerException e){
            System.out.println("ChunkFileService.getChunk(" + chunkID + ") returns null");
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

    public void applyChanges(List<Change> changes){
        changes.forEach(change -> {
            if(change.getType().getNumber() == Change.Type.CHUNK_VALUE){
                applyChunkChange(change);
            }
            else{
                applyPlayerChange(change);
            }
        });

        changedChunks.values().forEach(this::saveChunk);
        changedChunks.clear();
    }

    private void applyChunkChange(Change change){
        String chunkID = change.getId();

        switch (change.getEventValue()){
            case Change.Event.ADDED_VALUE -> {
                Chunk.Builder chunk = unpackValue(change, Chunk.class).toBuilder();
                if(chunk != null) changedChunks.put(chunkID, chunk);
            }
            case Change.Event.REMOVED_VALUE -> removeChunk(chunkID);
            case Change.Event.UPDATED_VALUE -> {
                Chunk.Builder chunk = getOrLoadChunk(chunkID);

                List<String> oldChildren = chunk.getChildChunksList();

                if(!oldChildren.isEmpty()) oldChildren.forEach(this::removeChunk);

                chunk.clearChildChunks();

                chunk.addAllChildChunks(unpackValue(change, StringArrayWrapper.class).getValueList());
                changedChunks.put(chunkID, chunk);
            }
        }
    }

    private void applyPlayerChange(Change change){
        String[] ids = change.getId().split("\\s+"); //0: (Old)Chunk ID, 1: Player ID
        String chunkID = ids[0];
        String playerID = ids[1];
        Chunk.Builder chunk = getOrLoadChunk(chunkID);

        switch(change.getEventValue()){
            case Change.Event.ADDED_VALUE -> {
                Player player = unpackValue(change, Player.class);
                chunk.addPlayers(player);
            }
            case Change.Event.REMOVED_VALUE -> removePlayer(chunk, playerID);
            case Change.Event.UPDATED_VALUE -> {
                Player.Builder player = getPlayer(chunk, playerID);

                if(change.getKey().equals("chunk")){
                    String newChunkID = unpackValue(change, StringWrapper.class).getValue();
                    Chunk.Builder newChunk = getOrLoadChunk(newChunkID);

                    //Add player to new chunk
                    player.setChunk(newChunkID);
                    newChunk.addPlayers(player);
                    //Remove player from old chunk
                    removePlayer(chunk, playerID);
                }
                else if(change.getKey().equals("position")){
                    player.setPosition(unpackValue(change, Vector.class));
                }
            }
        }
    }

    private <T extends com.google.protobuf.Message> T unpackValue(Change change, Class<T> clazz){
        try{
            return change.getValue().unpack(clazz);
        }
        catch (InvalidProtocolBufferException e){
            e.printStackTrace();
            return null;
        }
    }

    private Chunk.Builder getOrLoadChunk(String chunkID){
        if(changedChunks.containsKey(chunkID)){
            return changedChunks.get(chunkID);
        }
        else{
            Chunk.Builder chunk = getChunk(chunkID).toBuilder();
            changedChunks.put(chunkID, chunk);
            return chunk;
        }
    }

    private void removePlayer(Chunk.Builder chunk, String playerID){
        chunk.removePlayers(indexOfPlayer(chunk, playerID));
    }

    private int indexOfPlayer(Chunk.Builder chunk, String playerID){
        int index = 0;
        for (Player player : chunk.getPlayersList()) {
            if(player.getId().equals(playerID)) return index;
            index++;
        }

        return -1;
    }

    private Player.Builder getPlayer(Chunk.Builder chunk, String playerID){
        for (Player.Builder player : chunk.getPlayersBuilderList()) {
            if(player.getId().equals(playerID)) return player;
        }

        return null;
    }

    /** Close all active FileStreams */
    public void close(){
        FileManager.tryClose(gameInfoOutput);
        FileManager.tryClose(gameInfoInput);
        chunkOutputs.values().forEach(FileManager::tryClose);
        chunkInputs.values().forEach(FileManager::tryClose);
    }
}
