package strategies.service;

import strategies.model.Chunk;
import strategies.model.Player;
import strategies.model.Vector;

import java.util.ArrayList;
import java.util.List;

import static strategies.Constants.*;

public class DynamicChunkService extends ChunkService {
    public DynamicChunkService() {
        super();
    }

    @Override
    public void createChunks(){
        rootChunk = newChunk(MAP_SIZE/2, MAP_SIZE/2, MAP_SIZE, "").getId();
    }

    @Override
    public Player.Builder addPlayer(Player.Builder player){
        Chunk.Builder chunk = findChunk(player.getPosition());
        chunk = getChunk(chunk.getId());
        player.setChunk(chunk.getId());
        chunk.addPlayers(player);

        if(!player.getIsBot()) playerChunk = player.getChunk();

        if(USE_CHANGE_FILE){
            changeFile.savePlayerAdded(player);
        }
        else{
            chunkFile.addChangedChunk(chunk);
        }

        checkChunk(chunk);
        return player;
    }

    @Override
    public void removePlayer(Player.Builder player){
        Chunk.Builder chunk = getChunk(player.getChunk());

        chunk.removePlayers(indexOfPlayer(chunk, player.getId()));

        if(USE_CHANGE_FILE){
            changeFile.savePlayerRemove(player.getId(), player.getChunk());
        }
        else{
           chunkFile.addChangedChunk(chunk);
        }

        checkChunk(chunk);
    }

    @Override
    public Player.Builder updatePlayer(Player.Builder player){
        if(USE_CHANGE_FILE){
            changeFile.savePlayerUpdate(player);
        }

        String oldChunkID = player.getChunk();
        getChunk(oldChunkID); //Load old chunk if needed

        if(checkPlayerChunkChange(player)){
            checkChunk(chunks.get(player.getChunk()));
            checkChunk(chunks.get(oldChunkID));
        }

        return player;
    }

    protected void checkChunk(Chunk.Builder chunk){
        if(chunk == null) return;
        Chunk.Builder parentChunk = chunks.get(chunk.getParentChunk());

        int chunkGroupSize = parentChunk == null? CHUNK_GROUP_MIN_ELEMENTS :
                parentChunk.getChildChunksList().stream().mapToInt(c -> getChunk(c).getPlayersCount()).sum();

        if(parentChunk != null && parentChunk.getChildChunksList().stream().filter(c -> chunks.get(c).getChildChunksCount() != 0).findFirst().isPresent()){
            chunkGroupSize = CHUNK_GROUP_MIN_ELEMENTS;
        }

        if(chunkGroupSize  < CHUNK_GROUP_MIN_ELEMENTS){
            mergeChunk(parentChunk);
        }
        else if(chunk.getPlayersCount() > CHUNK_MAX_ELEMENTS){
            splitChunk(chunk);
        }
    }

    protected void mergeChunk(Chunk.Builder parentChunk){
        parentChunk.getChildChunksList().forEach(chunkID -> {
            Chunk.Builder chunk = chunks.get(chunkID);

            //Move all players from chunk to parent chunk
            addPlayersToChunk(chunk.getPlayersBuilderList(), parentChunk, chunkID);

            //Remove chunk and save that
            removeChunk(chunk);
        });

        parentChunk.clearChildChunks();

        if(USE_CHANGE_FILE){
            changeFile.saveChunkUpdate(parentChunk.getId(), new ArrayList<>());
        }
    }

    protected void splitChunk(Chunk.Builder parentChunk){
        //Create new chunks with chunk as parent
        double newSize = parentChunk.getSize().getX()/2;
        List<Chunk.Builder> childChunks = List.of(
                newChunk(parentChunk.getPosition().getX() - newSize/2,parentChunk.getPosition().getY() - newSize/2, newSize, parentChunk.getId()),
                newChunk(parentChunk.getPosition().getX() + newSize/2,parentChunk.getPosition().getY() - newSize/2, newSize, parentChunk.getId()),
                newChunk(parentChunk.getPosition().getX() - newSize/2,parentChunk.getPosition().getY() + newSize/2, newSize, parentChunk.getId()),
                newChunk(parentChunk.getPosition().getX() + newSize/2,parentChunk.getPosition().getY() + newSize/2, newSize, parentChunk.getId())
        );

        //Split Players to new chunks
        parentChunk.getPlayersBuilderList().forEach(p -> addPlayerToChunk(p, findChunk(p.getPosition(), childChunks), parentChunk.getId()));
        parentChunk.clearPlayers();

        childChunks.forEach(c -> { if(c.getPlayersCount() > CHUNK_MAX_ELEMENTS) splitChunk(c); });

        //Save the rest of the changes
        if(USE_CHANGE_FILE){
            changeFile.saveChunkUpdate(parentChunk.getId(), parentChunk.getChildChunksList());
        }
        else{
            chunkFile.addChangedChunk(parentChunk);
        }
    }

    @Override
    protected Chunk.Builder findChunk(Vector position){
        List<Chunk.Builder> queue = new ArrayList<>();
        queue.add(chunks.get(rootChunk));

        Chunk.Builder chunk;
        while (!(inChunk(chunk = queue.get(0), position) && chunk.getChildChunksCount() == 0)) {
            queue.remove(0);
            queue.addAll(chunk.getChildChunksList().stream().map(chunks::get).filter(c -> c != null).toList());
            if(queue.isEmpty()) {
                chunk.clearChildChunks();
                return chunk;
            }
        }

        return chunk;
    }

    @Override
    protected Chunk.Builder findChunk(Vector position, String currentChunkID){
        if(currentChunkID.isEmpty()) return findChunk(position);

        Chunk.Builder currentChunk = getChunk(currentChunkID);
        if(currentChunk == null)  return findChunk(position);

        return inChunk(currentChunk, position) ? currentChunk : findChunk(position);
    }
}
