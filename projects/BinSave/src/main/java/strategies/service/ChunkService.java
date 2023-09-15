package strategies.service;

import com.google.protobuf.Any;
import strategies.model.*;
import strategies.model.Vector;

import java.util.*;

public class ChunkService {
    private final SaveService saveService;
    private final Game.Builder game;
    private final boolean dynamicChunkSize;
    private final boolean useChangeFile;

    private final Map<String, Chunk.Builder> chunks = new HashMap<>();

    public ChunkService(SaveService saveService, Game.Builder game, boolean dynamicChunkSize, boolean useChangeFile) {
        this.saveService = saveService;
        this.game = game;
        this.dynamicChunkSize = dynamicChunkSize;
        this.useChangeFile = useChangeFile;
    }

    //TODO: Setup Function to load chunks from last time
    //TODO: If useChangeFile load changes

    public void addPlayer(Player.Builder player){
        Chunk.Builder chunk = findChunk(player.getPosition());
        chunk.addPlayers(player);

        if(useChangeFile){
            saveService.saveChange(Change.newBuilder()
                    .setId(UUID.randomUUID().toString())
                    .setType(Change.Type.PLAYER)
                    .setEvent(Change.Event.ADDED)
                    .setValue(Any.pack(player.build()))
            );
        }
        else{
            if(dynamicChunkSize){
                checkChunk(chunk);
            }
            else{
                saveService.addChangedChunk(chunk);
            }
        }
    }

    public void removePlayer(){
        //TODO: Remove Player from correct chunk
        //TODO: If dynamic chunk size, check chunk
        //TODO: Add changed chunk
        //TODO: Add change in change file
    }

    public void updatePlayer(){
        //TODO: Update Player-Data
        //TODO: Add changed chunk
        //TODO: Add change in change file
    }

    private void checkChunk(Chunk.Builder chunk){
        //TODO: Check if chunk is too big/small
        //TODO: Add chunk changes to SaveService
    }

    private Chunk.Builder findChunk(Vector position){
        if(dynamicChunkSize){
            Queue<Chunk.Builder> queue = new PriorityQueue<>();
            queue.add(chunks.get(this.game.getInfo().getRootChunk()));

            Chunk.Builder chunk;
            while (!inChunk(chunk = queue.poll(), position) && chunk.getChildChunksCount() == 0) {
                queue.addAll(chunk.getChildChunksList().stream().map(s -> chunks.get(s)).toList());

                if(queue.peek() == null) return chunks.get(this.game.getInfo().getRootChunk()); //If queue empty return root chunk
            }

            return chunk;
        }
        else{
            return game.getChunksBuilderList().stream().filter(c -> inChunk(c,position)).findFirst().get();
        }
    }

    private boolean inChunk(Chunk.Builder chunk, Vector point){
        return Math.abs(chunk.getPosition().getX() - point.getX()) <= chunk.getSize().getX()/2
                && Math.abs(chunk.getPosition().getY() - point.getY()) <= chunk.getSize().getY()/2;
    }
}
