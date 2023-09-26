package strategies.service;

import strategies.model.Chunk;
import strategies.model.GameInfo;
import strategies.model.Player;
import strategies.model.Vector;

import java.util.*;

import static strategies.Constants.*;

public abstract class ChunkService {
    protected final ChunkFileService chunkFile = new ChunkFileService();
    protected final ChangeFileService changeFile = new ChangeFileService();

    protected final Map<String, Chunk> chunks = new HashMap<>();
    protected final List<String> loadedChunks = new ArrayList<>();
    protected String playerChunk = "";
    protected String rootChunk = "";

    private int chunkCount = 1;

    abstract void createChunks();

    public Player loadChunks(){
        if(USE_CHANGE_FILE) chunkFile.applyChanges();


        GameInfo info = chunkFile.loadGameInfo();
        //Load Chunk Info Data to chunks
        info.getChunksList().forEach(c -> {
            Chunk chunk = c.toChunk();
            chunks.put(chunk.getId(), chunk);
        });

        playerChunk = info.getPlayerChunk();
        rootChunk = info.getRootChunk();

        return getChunk(playerChunk).getPlayersList().stream().filter(p -> !p.getIsBot()).findFirst().get();
    }

    public void saveChunks(){
        if(!USE_CHANGE_FILE){
            chunkFile.saveChanges();
        }

        chunkFile.saveGameInfo(getGameInfo());
    }

    public void printGame(){
        if(!VERBOSE) return;

        for (Chunk chunk : getChunks()) {
            printChunk(chunk, "Chunk");
        }
    }

    abstract Player addPlayer(Player player);

    abstract void removePlayer(Player player);

    abstract Player updatePlayer(Player player);

    /**Checks if the chunk of the player has changed, if so the change is saved in the objects and files
     *
     * @return True if the chunk of the player has changed
     * */
    protected boolean checkPlayerChunkChange(Player player){
        String oldChunkID = player.getChunk();

        Chunk playerChunk = findChunk(player.getPosition(), oldChunkID);

        if(!oldChunkID.equals(playerChunk.getId())){
            //Load new chunk if needed
            playerChunk = getChunk(playerChunk.getId());

            //Remove player from old chunk
            Chunk oldChunk = getChunk(oldChunkID);
            int index = oldChunk == null? -1 : oldChunk.indexOfPlayer(player);

            if(index != -1) oldChunk.removePlayer(index);

            //Add player to new chunk
            playerChunk.addPlayer(player);
            player.setChunk(playerChunk.getId());

            //Save chunk changes
            if(USE_CHANGE_FILE){
                changeFile.savePlayerUpdate(player.getId(), oldChunkID, playerChunk.getId());
            }
            else{
                if(oldChunk != null) chunkFile.addChangedChunk(oldChunk);
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

    protected void addPlayerToChunk(Player player, Chunk chunk, String oldChunkID){
        player.setChunk(chunk.getId());
        chunk.addPlayer(player);

        if(!player.getIsBot()) playerChunk = chunk.getId();

        if(USE_CHANGE_FILE){
            changeFile.savePlayerUpdate(player.getId(), oldChunkID, chunk.getId());
        }
        else{
            chunkFile.addChangedChunk(chunk);
        }
    }

    protected void addPlayersToChunk(List<Player> players, Chunk chunk, String oldChunkID){
        players.forEach(p -> addPlayerToChunk(p, chunk, oldChunkID));
    }

    protected Chunk getChunk(String chunkID){
        if(loadedChunks.contains(chunkID)){
            return chunks.get(chunkID);
        }
        else{
            Chunk chunk = chunkFile.getChunk(chunkID);
            chunks.put(chunkID, chunk);
            loadedChunks.add(chunkID);
            return chunk;
        }
    }

    protected Chunk newChunk(double x, double y, double size, String parentChunkID){
        Chunk chunk = new Chunk()
                //.setId(UUID.randomUUID().toString())
                .setId(chunkCount + "")
                .setPosition(new Vector(x, y))
                .setSize(new Vector(size, size))
                .setParentChunk(parentChunkID);

        chunkCount++;

        //Add to maps
        chunks.put(chunk.getId(), chunk);
        loadedChunks.add(chunk.getId());

        //Add to game objects
        //Does not need to be added to change file or changed chunks!
        if(!parentChunkID.isEmpty()) chunks.get(parentChunkID).addChildChunk(chunk.getId());

        if(USE_CHANGE_FILE){
            changeFile.saveChunkAdded(chunk);
        }
        else{
            chunkFile.addChangedChunk(chunk);
        }

        return chunk;
    }

    protected void removeChunk(Chunk chunk){
        if(USE_CHANGE_FILE){
            changeFile.saveChunkRemoved(chunk.getId());
        }
        else{
            chunkFile.addRemovedChunk(chunk);
        }

        chunks.remove(chunk.getId());
        chunk.clearPlayers();
    }

    abstract Chunk findChunk(Vector position);

    abstract Chunk findChunk(Vector position, String currentChunkID);

    Chunk findChunk(Vector position, List<Chunk> chunks){
        Optional<Chunk> opt = chunks.stream().filter(c -> inChunk(c, position)).findFirst();

        if(opt.isPresent()) return opt.get();

        System.out.println("Position is not in the chunk list");
        return null;
    }

    protected boolean inChunk(Chunk chunk, Vector point){
        return Math.abs(chunk.getPosition().getX() - point.getX()) <= chunk.getSize().getX()/2
                && Math.abs(chunk.getPosition().getY() - point.getY()) <= chunk.getSize().getY()/2;
    }

    protected List<Chunk> getChunks(){
        return chunks.values().stream().toList();
    }

    private GameInfo getGameInfo(){
        GameInfo info = new GameInfo()
                .setPlayerChunk(playerChunk)
                .setRootChunk(rootChunk);

        getChunks().forEach(c -> info.addChunk(c.toInfo()));

        return info;
    }

    protected void printChunk(Chunk chunk, String title){
        System.out.println("\n========================" + title + "========================");
        System.out.println("Center: (" + chunk.getPosition().getX() + ", " + chunk.getPosition().getY() +
                ")      |        Size: (" + chunk.getSize().getX() + ", " + chunk.getSize().getY() + ")");
        System.out.println("ID: " + chunk.getId());
        if(!chunk.getParentChunk().isEmpty()) System.out.println("Parent: " + chunk.getParentChunk());

        if(chunk.getPlayersCount() > 0 && chunk.getPlayersCount() <= MAX_PLAYER_PRINT){
            System.out.println("Players (" + chunk.getPlayersCount() + "): ");
            chunk.getPlayersList().stream().map(p -> PRINT_TAB + p.getName() + " " + p.getId()).toList().forEach(System.out::println);
        }
        else if(chunk.getPlayersCount() > 0){
            System.out.println("Players (" + chunk.getPlayersCount() + ") ");
        }
    }

    public List<Player> getBots(){
        List<Player> bots = new ArrayList<>();

        chunks.values().forEach(c ->
            bots.addAll(
                chunkFile.getChunk(c.getId()).getPlayersList()
                    .stream().filter(Player::getIsBot).toList()
            )
        );

        return bots;
    }
}
