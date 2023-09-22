package strategies.service;

import strategies.model.Chunk;
import strategies.model.Player;
import strategies.model.Vector;

import java.util.stream.IntStream;

import static strategies.Constants.*;

public class StaticChunkService extends ChunkService {

    public StaticChunkService() {
        super();
    }

    @Override
    public void createChunks(){
        IntStream.range(0, STATIC_CHUNK_AMOUNT).forEach(y -> IntStream.range(0, STATIC_CHUNK_AMOUNT).forEach(x ->
                newChunk(x * STATIC_CHUNK_SIZE + STATIC_CHUNK_SIZE/2,
                y * STATIC_CHUNK_SIZE + STATIC_CHUNK_SIZE/2,
                STATIC_CHUNK_SIZE, "")
        ));
    }

    @Override
    public void addPlayer(Player.Builder player){
        Chunk.Builder chunk = findChunk(player.getPosition());
        player.setChunk(chunk.getId());
        chunk.addPlayers(player);

        if(!player.getIsBot()) playerChunk = player.getChunk();

        if(USE_CHANGE_FILE){
            changeFile.savePlayerAdded(player);
        }
        else{
            chunkFile.addChangedChunk(chunk);
        }
    }

    @Override
    public void removePlayer(Player.Builder player){
        Chunk.Builder chunk = chunks.get(player.getChunk());

        chunk.removePlayers(indexOfPlayer(player.getChunk(), player.getId()));

        if(USE_CHANGE_FILE){
            changeFile.savePlayerRemove(player.getId(), player.getChunk());
        }
        else{
            chunkFile.addChangedChunk(chunk);
        }
    }

    @Override
    public void updatePlayer(Player.Builder player){
        if(USE_CHANGE_FILE){
            changeFile.savePlayerUpdate(player);
        }

        checkPlayerChunkChange(player);
    }

    @Override
    protected Chunk.Builder findChunk(Vector position){
        return getChunks().stream().filter(c -> inChunk(c,position)).findFirst().get();
    }

    @Override
    protected Chunk.Builder findChunk(Vector position, String currentChunkID){
        if(currentChunkID.isEmpty()) return findChunk(position);

        Chunk.Builder currentChunk = chunks.get(currentChunkID);

        return inChunk(currentChunk, position)? currentChunk : findChunk(position);
    }
}
