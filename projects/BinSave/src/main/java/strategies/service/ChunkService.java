package strategies.service;

import strategies.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static strategies.Constants.USE_CHANGE_FILE;

public abstract class ChunkService {
    protected final ChunkFileService chunkFile = new ChunkFileService();
    protected final ChangeFileService changeFile = new ChangeFileService();

    protected final Map<String, Chunk.Builder> chunks = new HashMap<>();
    protected String playerChunk = "";
    protected String rootChunk = "";

    abstract void createChunks();

    public Player.Builder loadChunks(){
        if(USE_CHANGE_FILE){
            applyChanges();
        }

        GameInfo info = chunkFile.loadGameInfo();
        //Load Chunk Info Data to chunks
        info.getChunksList().forEach(c -> {
            Chunk.Builder chunk = infoToChunk(c);
            chunks.put(chunk.getId(), chunk);
        });

        playerChunk = info.getPlayerChunk();
        rootChunk = info.getRootChunk();

        //Load players of player chunk
        checkChunkPlayers(playerChunk);

        return chunks.get(playerChunk).getPlayersBuilderList().stream().filter(p -> !p.getIsBot()).findFirst().get();
    }

    public void saveChunks(){
        if(!USE_CHANGE_FILE){
            chunkFile.saveChanges();
        }

        chunkFile.saveGameInfo(getGameInfo());
    }

    public void printGame(){
        for (Chunk.Builder chunk : getChunks()) {
            printChunk(chunk);
        }
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
        Chunk.Builder playerChunk = findChunk(player.getPosition(), oldChunkID);

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

        if(!player.getIsBot()) playerChunk = chunk.getId();

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
            chunks.put(chunkID, chunk);
            return chunk;
        }
    }

    protected void checkChunkPlayers(String chunkID){
        Chunk.Builder cachedChunk = chunks.get(chunkID);

        if(cachedChunk.getPlayersCount() == 0) {
            chunks.remove(chunkID);
            chunks.put(chunkID, chunkFile.getChunk(chunkID).toBuilder());
        }
    }

    protected Chunk.Builder newChunk(float x, float y, float size, String parentChunkID){
        Chunk.Builder chunk = Chunk.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setPosition(Vector.newBuilder().setX(x).setY(y))
                .setSize(Vector.newBuilder().setX(size).setY(size))
                .setParentChunk(parentChunkID);

        ChunkInfo.Builder info = chunkToInfo(chunk);

        //Add to maps
        chunks.put(chunk.getId(), chunk);

        //Add to game objects
        if(!parentChunkID.equals("")) chunks.get(parentChunkID).addChildChunks(chunk.getId());

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

        chunks.remove(chunk.getId());
        chunk.clearPlayers();
    }

    private ChunkInfo.Builder chunkToInfo(Chunk.Builder chunk){
        return ChunkInfo.newBuilder()
                .setId(chunk.getId())
                .setPosition(chunk.getPosition())
                .setSize(chunk.getSize())
                .setParentChunk(chunk.getParentChunk())
                .addAllChildChunks(chunk.getChildChunksList());
    }

    private Chunk.Builder infoToChunk(ChunkInfo info){
        return Chunk.newBuilder()
                .setId(info.getId())
                .setPosition(info.getPosition())
                .setSize(info.getSize())
                .setParentChunk(info.getParentChunk())
                .addAllChildChunks(info.getChildChunksList());
    }

    abstract Chunk.Builder findChunk(Vector position);

    abstract Chunk.Builder findChunk(Vector position, String currentChunkID);

    Chunk.Builder findChunk(Vector position, List<Chunk.Builder> chunks){
        return chunks.stream().filter(c -> inChunk(c, position)).findFirst().get();
    }

    protected boolean inChunk(Chunk.Builder chunk, Vector point){
        return Math.abs(chunk.getPosition().getX() - point.getX()) <= chunk.getSize().getX()/2
                && Math.abs(chunk.getPosition().getY() - point.getY()) <= chunk.getSize().getY()/2;
    }

    protected Player.Builder getPlayerBuilder(Chunk.Builder chunk, String playerID){
        return chunk.getPlayersBuilderList().stream().filter(p -> p.getId().equals(playerID)).findFirst().get();
    }

    protected int indexOfPlayer(String chunkID, String playerID){
        int index = 0;
        for (Player.Builder p : chunks.get(chunkID).getPlayersBuilderList()) {
            if(p.getId().equals(playerID)) return index;
            index++;
        }

        return -1;
    }

    protected List<Chunk.Builder> getChunks(){
        return chunks.entrySet().stream().map(e -> e.getValue()).toList();
    }

    private GameInfo getGameInfo(){
        GameInfo.Builder info = GameInfo.newBuilder()
                .setPlayerChunk(playerChunk)
                .setRootChunk(rootChunk);

        getChunks().forEach(c -> info.addChunks(chunkToInfo(c)));

        return info.build();
    }

    protected void printChunk(Chunk.Builder chunk){
        System.out.println("\n============================CHUNK============================");
        System.out.println("ID: " + chunk.getId() + " Center: (" + chunk.getPosition().getX() + ", " + chunk.getPosition().getY() + ")");
        if(chunk.getParentChunk() != null || chunk.getParentChunk().equals("")) System.out.println("Parent: " + chunk.getParentChunk());

        if(chunk.getPlayersCount() > 0){
            System.out.println("Children(" + chunk.getPlayersCount() + "): ");
            chunk.getPlayersList().stream().map(p -> p.getName() + " " + p.getId()).toList().forEach(System.out::println);
        }
    }

    public void close(){
        changeFile.close();
        chunkFile.close();
    }
}
