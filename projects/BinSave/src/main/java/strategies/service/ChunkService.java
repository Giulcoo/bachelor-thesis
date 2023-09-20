package strategies.service;

import com.google.protobuf.InvalidProtocolBufferException;
import strategies.Constants;
import strategies.model.*;
import strategies.model.Vector;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static strategies.Constants.*;

public abstract class ChunkService {
    protected final ChunkFileService chunkFile = new ChunkFileService();
    protected final ChangeFileService changeFile = new ChangeFileService();
    protected final Game.Builder game;
    protected final Map<String, Chunk.Builder> chunks = new HashMap<>();
    protected final Map<String, ChunkInfo.Builder> infos = new HashMap<>();

    public ChunkService(Game.Builder game) {
        this.game = game;

        //Create data folder, if it does not exist
        try {
            Path dataPath = Path.of(Constants.DATA_PATH);
            if(!Files.exists(dataPath)) Files.createDirectory(dataPath);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    abstract void createChunks();

    public void loadChunks(){
        if(USE_CHANGE_FILE){
            applyChanges();
        }

        //Load Chunk Info Data
        this.game.setInfo(chunkFile.loadGameInfo());

        //Set Chunks (without any players loaded) and add to game
        this.game.getInfoBuilder().getChunksBuilderList().forEach(c -> {
            if(!chunks.containsKey(c.getId())){
                Chunk.Builder chunk = infoToChunk(c);
                this.game.addChunks(chunk);
                chunks.put(chunk.getId(), chunk);
            }
        });

        //Load players of player chunk
        checkChunkPlayers(this.game.getInfo().getPlayerChunk());
    }

    public void saveChunks(){
        if(!USE_CHANGE_FILE){
            chunkFile.saveChanges();
        }

        chunkFile.saveGameInfo(game.getInfoBuilder());
    }


    private void applyChanges(){
        //Set changes
        changeFile.getChanges().forEach(change -> {
            if(change.getType().getNumber() == Change.Type.CHUNK_VALUE){
                applyChunkChange(change);
            }
            else{
                applyPlayerChange(change);
            }
        });

        //Save changes
        chunkFile.addChangedChunks(chunks.entrySet().stream().map(entry -> entry.getValue()).toList());
        chunkFile.saveChanges();
    }

    private void applyChunkChange(Change change){
        String chunkID = change.getId();

        switch (change.getEventValue()){
            case Change.Event.ADDED_VALUE -> {
                Chunk.Builder chunk = changeFile.unpackValue(change, Chunk.class).toBuilder();
                if(chunk != null) chunks.put(chunkID, chunk);
            }
            case Change.Event.REMOVED_VALUE -> {
                chunkFile.addRemovedChunk(chunkID);
            }
            case Change.Event.UPDATED_VALUE -> {
                Chunk.Builder chunk = getChunk(chunkID);

                List<String> oldChildren = chunk.getChildChunksList();
                chunk.clearChildChunks();

                chunk.addAllChildChunks(changeFile.unpackValue(change, StringArrayWrapper.class).getValueList());
            }
        }
    }

    private void applyPlayerChange(Change change){
        String[] ids = change.getId().split("\\s+"); //0: (Old)Chunk ID, 1: Player ID
        Chunk.Builder chunk = getChunk(ids[0]);

        switch(change.getEventValue()){
            case Change.Event.ADDED_VALUE -> {
                Player player = changeFile.unpackValue(change, Player.class);
                chunk.addPlayers(player);
            }
            case Change.Event.REMOVED_VALUE -> {

            }
            case Change.Event.UPDATED_VALUE -> {
                Player.Builder player = getPlayerBuilder(chunk, ids[1]);

                if(change.getKey().equals("chunk")){
                    String newChunkID = changeFile.unpackValue(change, StringWrapper.class).getValue();
                    Chunk.Builder newChunk = getChunk(newChunkID);

                    player.setChunk(newChunkID);
                    newChunk.addPlayers(player);
                    chunk.removePlayers(indexOfPlayer(chunk.getId(), player.getId()));
                }
                else if(change.getKey().equals("position")){
                    player.setPosition(changeFile.unpackValue(change, Vector.class));
                }
            }
        }
    }

    abstract void addPlayer(Player.Builder player);

    abstract void removePlayer(Player.Builder player);

    abstract void updatePlayer(Player.Builder player);

    /**Checks if the chunk of the player has changed, if so the change is saved in the objects and files
     *
     * @return True if the chunk of the player has changed
     * */
    protected boolean checkPlayerChunkChange(Player.Builder player){
        String oldChunkID = player.getChunk();
        Chunk.Builder playerChunk = findChunk(player.getPosition());

        if(!oldChunkID.equals(playerChunk.getId())){
            //Load new chunk if needed
            checkChunkPlayers(playerChunk.getId());

            //Remove player from old chunk
            Chunk.Builder oldChunk = chunks.get(oldChunkID);
            oldChunk.removePlayers(indexOfPlayer(oldChunkID, player.getId()));

            //Add player to new chunk
            playerChunk.addPlayers(player);
            player.setChunk(playerChunk.getId());

            //Save chunk changes
            if(USE_CHANGE_FILE){
                changeFile.savePlayerUpdate(player.getId(), oldChunkID, playerChunk.getId());
            }
            else{
                chunkFile.addChangedChunk(oldChunk);
                chunkFile.addChangedChunk(playerChunk);
            }

            return true;
        }
        else{
            if(!USE_CHANGE_FILE){
                chunkFile.addChangedChunk(playerChunk);
            }

            return false;
        }
    }

    protected void addPlayerToChunk(Player.Builder player, Chunk.Builder chunk, String oldChunkID){
        player.setChunk(chunk.getId());
        chunk.addPlayers(player);

        if(!player.getIsBot()) game.getInfoBuilder().setPlayerChunk(chunk.getId());

        if(USE_CHANGE_FILE){
            changeFile.savePlayerUpdate(player.getId(), oldChunkID, chunk.getId());
        }
        else{
            chunkFile.addChangedChunk(chunk);
        }
    }

    protected void addPlayersToChunk(List<Player.Builder> players, Chunk.Builder chunk, String oldChunkID){
        players.forEach(p -> addPlayerToChunk(p, chunk, oldChunkID));
    }

    private Chunk.Builder getChunk(String chunkID){
        if(chunks.containsKey(chunkID)){
            return chunks.get(chunkID);
        }
        else{
            Chunk.Builder chunk = chunkFile.getChunk(chunkID).toBuilder();
            this.game.addChunks(chunk);
            chunks.put(chunkID, chunk);
            return chunk;
        }
    }

    protected void checkChunkPlayers(String chunkID){
        Chunk.Builder cachedChunk = chunks.get(chunkID);

        if(cachedChunk.getPlayersCount() == 0) cachedChunk.addAllPlayers(chunkFile.getChunk(chunkID).getPlayersList());
    }

    protected Chunk.Builder newChunk(float x, float y, float size, Chunk.Builder parentChunk){
        Chunk.Builder chunk = Chunk.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setPosition(Vector.newBuilder().setX(x).setY(y))
                .setSize(Vector.newBuilder().setX(size).setY(size))
                .setParentChunk(parentChunk == null ? "" : parentChunk.getId());

        ChunkInfo.Builder info = chunkToInfo(chunk);

        //Add to maps
        chunks.put(chunk.getId(), chunk);
        infos.put(chunk.getId(), info);

        //Add to game objects
        game.addChunks(chunk);
        game.getInfoBuilder().addChunks(info);
        if(parentChunk != null) parentChunk.addChildChunks(chunk.getId());

        if(USE_CHANGE_FILE){
            changeFile.saveChunkAdded(chunk);
        }
        else{
            chunkFile.addChangedChunk(chunk);
        }

        return chunk;
    }

    protected void removeChunk(Chunk.Builder chunk){
        if(USE_CHANGE_FILE){
            changeFile.saveChunkRemoved(chunk.getId());
        }
        else{
            chunkFile.addRemovedChunk(chunk);
        }

        game.removeChunks(indexOfChunk(chunk));
        game.getInfoBuilder().removeChunks(indexOfChunk(infos.get(chunk.getId())));
        chunk.clearPlayers();

        chunks.remove(chunk.getId());
        infos.remove(chunk.getId());
    }

    private ChunkInfo.Builder chunkToInfo(Chunk.Builder chunk){
        return ChunkInfo.newBuilder()
                .setId(chunk.getId())
                .setPosition(chunk.getPosition())
                .setSize(chunk.getSize())
                .setParentChunk(chunk.getParentChunk())
                .addAllChildChunks(chunk.getChildChunksList());
    }

    private Chunk.Builder infoToChunk(ChunkInfo.Builder info){
        return Chunk.newBuilder()
                .setId(info.getId())
                .setPosition(info.getPosition())
                .setSize(info.getSize())
                .setParentChunk(info.getParentChunk())
                .addAllChildChunks(info.getChildChunksList());
    }

    abstract Chunk.Builder findChunk(Vector position);

    Chunk.Builder findChunk(Vector position, List<Chunk.Builder> chunks){
        return chunks.stream().filter(c -> inChunk(c, position)).findFirst().get();
    }

    protected boolean inChunk(Chunk.Builder chunk, Vector point){
        return Math.abs(chunk.getPosition().getX() - point.getX()) <= chunk.getSize().getX()/2
                && Math.abs(chunk.getPosition().getY() - point.getY()) <= chunk.getSize().getY()/2;
    }

    protected Player getPlayer(String chunkID, String playerID){
        return chunks.get(chunkID).getPlayersList().stream().filter(p -> p.getId().equals(playerID)).findFirst().get();
    }

    protected Player.Builder getPlayerBuilder(Chunk.Builder chunk, String playerID){
        return chunk.getPlayersBuilderList().stream().filter(p -> p.getId().equals(playerID)).findFirst().get();
    }

    protected int indexOfPlayer(String chunkID, String playerID){
        return chunks.get(chunkID).getPlayersList().indexOf(getPlayer(chunkID, playerID));
    }

    protected int indexOfChunk(Chunk.Builder chunk){
        return game.getChunksBuilderList().indexOf(chunk);
    }

    protected int indexOfChunk(ChunkInfo.Builder chunk){
        return game.getInfoBuilder().getChunksBuilderList().indexOf(chunk);
    }

    public void close(){
        changeFile.close();
        chunkFile.close();
    }
}
