package strategies.service;

import strategies.model.*;
import strategies.model.Vector;

import java.util.*;

import static strategies.Constants.CHUNK_GROUP_MIN_ELEMENTS;
import static strategies.Constants.CHUNK_MAX_ELEMENTS;

public class StaticChunkService extends ChunkService {

    public StaticChunkService(Game.Builder game, boolean useChangeFile) {
        super(game, false, useChangeFile);
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
        //TODO: Remove Player from correct chunk
        //TODO: Add changed chunk
        //TODO: Add change in change file
    }

    @Override
    public void updatePlayer(Player.Builder player){
        //TODO: Update Player-Data
        //TODO: Add changed chunk
        //TODO: Add change in change file
    }

    @Override
    protected Chunk.Builder findChunk(Vector position){
        return game.getChunksBuilderList().stream().filter(c -> inChunk(c,position)).findFirst().get();
    }
}
