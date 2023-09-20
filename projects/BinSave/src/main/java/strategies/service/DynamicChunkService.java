package strategies.service;

import com.google.protobuf.Any;
import strategies.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import static strategies.Constants.*;

public class DynamicChunkService extends ChunkService {
    public DynamicChunkService(Game.Builder game) {
        super(game);
    }

    @Override
    public void createChunks(){
        newChunk(MAP_SIZE/2, MAP_SIZE/2, MAP_SIZE, null);
    }

    @Override
    public void addPlayer(Player.Builder player){
        Chunk.Builder chunk = findChunk(player.getPosition());
        chunk.addPlayers(player);
        player.setChunk(chunk.getId());

        if(!player.getIsBot()) game.getInfoBuilder().setPlayerChunk(player.getChunk());

        if(USE_CHANGE_FILE){
            changeFile.savePlayerAdded(player);
        }
        else{
            chunkFile.addChangedChunk(chunk);
        }

        checkChunk(chunk);
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

        checkChunk(chunk);
    }

    @Override
    public void updatePlayer(Player.Builder player){
        if(USE_CHANGE_FILE){
            changeFile.savePlayerUpdate(player);
        }

        String oldChunkID = player.getId();

        if(checkPlayerChunkChange(player)){
            checkChunk(chunks.get(player.getId()));
            checkChunk(chunks.get(oldChunkID));
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
        parentChunk.getChildChunksList().forEach(chunkID -> {
            Chunk.Builder chunk = chunks.get(chunkID);

            //Move all players from chunk to parent chunk
            addPlayersToChunk(chunk.getPlayersBuilderList(), parentChunk, chunkID);

            //Remove chunk and save that
            removeChunk(chunk);
        });

        parentChunk.clearChildChunks();
        infos.get(parentChunk.getId()).clearChildChunks();

        if(USE_CHANGE_FILE){
            changeFile.saveChunkUpdate(parentChunk.getId(), new ArrayList<>());
        }
    }

    protected void splitChunk(Chunk.Builder parentChunk){
        //Create new chunks with chunk as parent
        float newSize = parentChunk.getSize().getX()/2;
        List<Chunk.Builder> childChunks = List.of(
                newChunk(parentChunk.getPosition().getX() - newSize/2,parentChunk.getPosition().getY() - newSize/2, newSize, parentChunk),
                newChunk(parentChunk.getPosition().getX() + newSize/2,parentChunk.getPosition().getY() - newSize/2, newSize, parentChunk),
                newChunk(parentChunk.getPosition().getX() - newSize/2,parentChunk.getPosition().getY() + newSize/2, newSize, parentChunk),
                newChunk(parentChunk.getPosition().getX() + newSize/2,parentChunk.getPosition().getY() + newSize/2, newSize, parentChunk)
        );

        //Split Players to new chunks
        parentChunk.getPlayersBuilderList().forEach(p -> addPlayerToChunk(p, findChunk(p.getPosition(), childChunks), parentChunk.getId()));
        parentChunk.clearPlayers();

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
