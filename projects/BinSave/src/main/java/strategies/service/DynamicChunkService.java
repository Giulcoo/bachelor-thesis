package strategies.service;

import strategies.model.*;
import strategies.model.Vector;

import java.util.*;

import static strategies.Constants.CHUNK_GROUP_MIN_ELEMENTS;
import static strategies.Constants.CHUNK_MAX_ELEMENTS;

public class DynamicChunkService extends ChunkService {
    public DynamicChunkService(Game.Builder game, boolean useChangeFile) {
        super(game, true, useChangeFile);
    }

    @Override
    public void addPlayer(Player.Builder player){
        Chunk.Builder chunk = findChunk(player.getPosition());
        chunk.addPlayers(player);
        player.setChunk(chunk.getId());

        if(useChangeFile){
            changeFile.savePlayerAdded(player);
        }
        else{
            chunkFile.addChangedChunk(chunk);
        }

        checkChunk(chunk);
    }

    @Override
    public void removePlayer(String chunkID, String playerID){
        Chunk.Builder chunk = chunks.get(chunkID);
        Player player = chunk.getPlayersList().stream().filter(p -> p.getId().equals(playerID)).findFirst().get();

        chunk.removePlayers(chunk.getPlayersList().indexOf(player));

        if(useChangeFile){
            changeFile.savePlayerRemove(playerID, chunkID);
        }
        else{
           chunkFile.addChangedChunk(chunk);
        }

        checkChunk(chunk);
    }

    @Override
    public void updatePlayer(Player.Builder player){
        if(useChangeFile){
            changeFile.savePlayerUpdate(player.getId(), player.getPosition());
        }

        String oldChunkID = player.getChunk();
        Chunk.Builder playerChunk = findChunk(player.getPosition());

        if(!oldChunkID.equals(playerChunk.getId())){
            //Remove player from old chunk
            Chunk.Builder oldChunk = chunks.get(oldChunkID);

            Player buildPlayer = oldChunk.getPlayersList().stream().filter(p -> p.getId().equals(player.getId())).findFirst().get();
            oldChunk.removePlayers(oldChunk.getPlayersList().indexOf(buildPlayer));

            //Add player to new chunk
            playerChunk.addPlayers(player);
            player.setChunk(playerChunk.getId());

            //Save chunk changes
            if(useChangeFile){
                changeFile.savePlayerUpdate(player.getId(), playerChunk.getId());
            }
            else{
                chunkFile.addChangedChunk(oldChunk);
                chunkFile.addChangedChunk(playerChunk);
            }
        }
        else{
            if(!useChangeFile){
                chunkFile.addChangedChunk(playerChunk);
            }
        }
    }

    protected void checkChunk(Chunk.Builder chunk){
        Chunk.Builder parentChunk = chunks.get(chunk.getParentChunk());
        int chunkGroupSize = parentChunk == null? CHUNK_GROUP_MIN_ELEMENTS :
                parentChunk.getChildChunksList().stream().mapToInt(c -> chunks.get(c).getPlayersCount()).sum();

        if(chunkGroupSize  < CHUNK_GROUP_MIN_ELEMENTS){
            mergeChunk(parentChunk);
        }
        else if(chunk.getPlayersCount() > CHUNK_MAX_ELEMENTS){
            splitChunk(chunk);
        }
    }

    protected void mergeChunk(Chunk.Builder parentChunk){
        if(!useChangeFile){
            chunkFile.addChangedChunk(parentChunk);
            chunkFile.addRemovedChunks(parentChunk.getChildChunksList().stream().map(c -> chunks.get(c)).toList());
        }

        parentChunk.getChildChunksList().forEach(c -> {
            Chunk.Builder chunk = chunks.get(c);
            ChunkInfo.Builder info = infos.get(c);
            parentChunk.addAllPlayers(chunk.getPlayersList());
            chunk.getPlayersBuilderList().forEach(p -> p.setChunk(info.getId()));

            if(useChangeFile){
                changeFile.saveChunkRemoved(c);
                chunk.getPlayersList().forEach(p -> changeFile.savePlayerUpdate(p.getId(), parentChunk.getId()));
            }

            chunk.clearPlayers();
            chunks.remove(c);
            infos.remove(c);
            game.removeChunks(game.getChunksBuilderList().indexOf(chunk));
            game.getInfoBuilder().removeChunks(game.getChunksBuilderList().indexOf(info));
        });

        parentChunk.clearChildChunks();
        infos.get(parentChunk.getId()).clearChildChunks();
    }

    protected void splitChunk(Chunk.Builder chunk){
        //Create new chunks with chunk as parent
        float newSize = chunk.getSize().getX()/2;
        List<Chunk.Builder> childChunks = new ArrayList<>();

        childChunks.add(newChunk(chunk.getPosition().getX() - newSize/2,chunk.getPosition().getY() - newSize/2, newSize, chunk.getId()));
        childChunks.add(newChunk(chunk.getPosition().getX() + newSize/2,chunk.getPosition().getY() - newSize/2, newSize, chunk.getId()));
        childChunks.add(newChunk(chunk.getPosition().getX() - newSize/2,chunk.getPosition().getY() + newSize/2, newSize, chunk.getId()));
        childChunks.add(newChunk(chunk.getPosition().getX() + newSize/2,chunk.getPosition().getY() + newSize/2, newSize, chunk.getId()));

        //Add new chunks to data structures
        childChunks.forEach(c -> {
            ChunkInfo.Builder info = chunkToInfo(c);

            game.addChunks(c);
            game.getInfoBuilder().addChunks(info);
            chunk.addChildChunks(c.getId());
            chunks.put(c.getId(), c);
            infos.put(c.getId(), info);
        });

        //Split Players to new chunks
        chunk.getPlayersBuilderList().forEach(p -> {
            childChunks.stream().filter(c -> inChunk(c, p.getPosition())).findFirst().get().addPlayers(p);
        });

        chunk.clearPlayers();

        //Save changes
        if(useChangeFile){
            changeFile.saveChunkUpdate(chunk.getId(), chunk.getChildChunksList());

            childChunks.forEach(c -> {
                changeFile.saveChunkAdded(c);
                c.getPlayersList().forEach(p -> changeFile.savePlayerUpdate(p.getId(), c.getId()));
            });
        }
        else{

        }
    }

    @Override
    protected Chunk.Builder findChunk(Vector position){
        Queue<Chunk.Builder> queue = new PriorityQueue<>();
        queue.add(chunks.get(this.game.getInfo().getRootChunk()));

        Chunk.Builder chunk;
        while (!inChunk(chunk = queue.poll(), position) && chunk.getChildChunksCount() == 0) {
            queue.addAll(chunk.getChildChunksList().stream().map(s -> chunks.get(s)).toList());

            if(queue.peek() == null) return chunks.get(this.game.getInfo().getRootChunk()); //If queue empty return root chunk
        }

        return chunk;
    }
}
