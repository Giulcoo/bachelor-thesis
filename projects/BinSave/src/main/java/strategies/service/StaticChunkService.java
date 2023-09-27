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
        for(int y = 0; y < STATIC_CHUNK_AMOUNT; y++){
            for(int x = 0; x < STATIC_CHUNK_AMOUNT; x++){
                newChunk(x * STATIC_CHUNK_SIZE + STATIC_CHUNK_SIZE/2,
                        y * STATIC_CHUNK_SIZE + STATIC_CHUNK_SIZE/2,
                        STATIC_CHUNK_SIZE, "");
            }
        }
    }

    @Override
    public Player.Builder addPlayer(Player.Builder player){
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
    }

    @Override
    public Player.Builder updatePlayer(Player.Builder player){
        if(USE_CHANGE_FILE){
            changeFile.savePlayerUpdate(player);
        }

        checkPlayerChunkChange(player);

        return player;
    }

    @Override
    protected Chunk.Builder findChunk(Vector position){
        return chunks.values().stream().filter(c -> inChunk(c, position)).findFirst().get();
    }

    @Override
    protected Chunk.Builder findChunk(Vector position, String currentChunkID){
        if(currentChunkID.isEmpty()) return findChunk(position);

        Chunk.Builder currentChunk = chunks.get(currentChunkID);

        return inChunk(currentChunk, position)? currentChunk : findChunk(position);
    }
}
