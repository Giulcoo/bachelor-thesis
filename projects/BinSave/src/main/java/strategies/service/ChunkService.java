package strategies.service;

import com.google.protobuf.InvalidProtocolBufferException;
import strategies.model.*;
import strategies.model.Vector;

import java.util.*;

import static strategies.Constants.*;

public abstract class ChunkService {
    protected final ChunkFileService chunkFile = new ChunkFileService();
    protected final ChangeFileService changeFile = new ChangeFileService();
    protected final Game.Builder game;
    protected final boolean dynamicChunkSize;
    protected final boolean useChangeFile;
    protected final Map<String, Chunk.Builder> chunks = new HashMap<>();
    protected final Map<String, ChunkInfo.Builder> infos = new HashMap<>();

    public ChunkService(Game.Builder game, boolean dynamicChunkSize, boolean useChangeFile) {
        this.game = game;
        this.dynamicChunkSize = dynamicChunkSize;
        this.useChangeFile = useChangeFile;
    }

    abstract void createChunks();

    public void loadChunks(){
        if(useChangeFile){
            applyChanges();
        }

        //TODO: Load data from chunks
    }

    public void saveChunks(){
        chunkFile.saveChanges();
    }


    private void applyChanges(){
        changeFile.getChanges().forEach(change -> {
            if(change.getType().getNumber() == Change.Type.CHUNK_VALUE){
                applyChunkChange(change);
            }
            else{
                applyPlayerChange(change);
            }
        });

        chunkFile.addChangedChunks(chunks.entrySet().stream().map(entry -> entry.getValue()).toList());
        chunkFile.saveChanges();
        chunks.clear();
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

    abstract void removePlayer(String chunkID, String playerID);

    abstract void updatePlayer(Player.Builder player);

    /**Checks if the chunk of the player has changed, if so the change is saved in the objects and files
     *
     * @return True if the chunk of the player has changed
     * */
    protected boolean checkPlayerChunkChange(Player.Builder player){
        String oldChunkID = player.getChunk();
        Chunk.Builder playerChunk = findChunk(player.getPosition());

        if(!oldChunkID.equals(playerChunk.getId())){
            //Remove player from old chunk
            Chunk.Builder oldChunk = chunks.get(oldChunkID);
            oldChunk.removePlayers(indexOfPlayer(oldChunkID, player.getId()));

            //Add player to new chunk
            playerChunk.addPlayers(player);
            player.setChunk(playerChunk.getId());

            //Save chunk changes
            if(useChangeFile){
                changeFile.savePlayerUpdate(player.getId(), oldChunkID, playerChunk.getId());
            }
            else{
                chunkFile.addChangedChunk(oldChunk);
                chunkFile.addChangedChunk(playerChunk);
            }

            return true;
        }
        else{
            if(!useChangeFile){
                chunkFile.addChangedChunk(playerChunk);
            }

            return false;
        }
    }

    protected void addPlayerToChunk(Player.Builder player, Chunk.Builder chunk, String oldChunkID){
        player.setChunk(chunk.getId());
        chunk.addPlayers(player);

        if(useChangeFile){
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
            chunks.put(chunkID, chunk);
            return chunk;
        }
    }

    protected Chunk.Builder newChunk(float x, float y, float size, Chunk.Builder parentChunk){
        Chunk.Builder chunk = Chunk.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setPosition(Vector.newBuilder().setX(x).setY(y))
                .setSize(Vector.newBuilder().setX(size).setY(size))
                .setParentChunk(parentChunk == null ? null : parentChunk.getId());

        ChunkInfo.Builder info = chunkToInfo(chunk);

        //Add to maps
        chunks.put(chunk.getId(), chunk);
        infos.put(chunk.getId(), info);

        //Add to game objects
        game.addChunks(chunk);
        game.getInfoBuilder().addChunks(info);
        if(parentChunk != null) parentChunk.addChildChunks(chunk.getId());

        if(useChangeFile){
            changeFile.saveChunkAdded(chunk);
        }
        else{
            chunkFile.addChangedChunk(chunk);
        }

        return chunk;
    }

    protected void removeChunk(Chunk.Builder chunk){
        if(useChangeFile){
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
