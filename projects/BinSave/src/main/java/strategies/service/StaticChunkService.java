package strategies.service;

import strategies.model.Chunk;
import strategies.model.Game;
import strategies.model.Player;
import strategies.model.Vector;

import java.util.stream.IntStream;

import static strategies.Constants.*;

public class StaticChunkService extends ChunkService {

    public StaticChunkService(Game.Builder game, boolean useChangeFile) {
        super(game, false, useChangeFile);
    }

    @Override
    public void createChunks(){
        IntStream.range(0, STATIC_CHUNK_AMOUNT).forEach(y -> {
            IntStream.range(0, STATIC_CHUNK_AMOUNT).forEach(x -> {
                newChunk(x * STATIC_CHUNK_SIZE + STATIC_CHUNK_SIZE/2,
                        y * STATIC_CHUNK_SIZE + STATIC_CHUNK_SIZE/2,
                        STATIC_CHUNK_SIZE, null);
            });
        });
    }

    @Override
    public void addPlayer(Player.Builder player){
        Chunk.Builder chunk = findChunk(player.getPosition());
        chunk.addPlayers(player);

        if(useChangeFile){
            changeFile.savePlayerAdded(player);
        }
        else{
            chunkFile.addChangedChunk(chunk);
        }
    }

    @Override
    public void removePlayer(String chunkID, String playerID){
        Chunk.Builder chunk = chunks.get(chunkID);

        chunk.removePlayers(indexOfPlayer(chunkID, playerID));

        if(useChangeFile){
            changeFile.savePlayerRemove(playerID, chunkID);
        }
        else{
            chunkFile.addChangedChunk(chunk);
        }
    }

    @Override
    public void updatePlayer(Player.Builder player){
        if(useChangeFile){
            changeFile.savePlayerUpdate(player);
        }

        checkPlayerChunkChange(player);
    }

    @Override
    protected Chunk.Builder findChunk(Vector position){
        return game.getChunksBuilderList().stream().filter(c -> inChunk(c,position)).findFirst().get();
    }
}
