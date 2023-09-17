package strategies.service;

import strategies.model.*;
import strategies.model.Vector;

import java.util.*;

import static strategies.Constants.*;

public class ChunkService {
    protected final ChunkFileService chunkFile = new ChunkFileService();
    protected final ChangeFileService changeFile = new ChangeFileService();
    protected final Game.Builder game;
    protected final boolean dynamicChunkSize;
    protected final boolean useChangeFile;
    protected final Map<String, Chunk.Builder> chunks = new HashMap<>();
    protected final Map<String, ChunkInfo.Builder> infos = new HashMap<>();

    public ChunkService(Game.Builder game, boolean dynamicChunkSize, boolean useChangeFile) {
        this.game = game;
        this.dynamicChunkSize = dynamicChunkSize;
        this.useChangeFile = useChangeFile;
    }

    //TODO: Setup Function to load chunks from last time
    //TODO: If useChangeFile load changes

    public void saveChunks(){

    }

    public void loadChunks(){

    }

    public void addPlayer(Player.Builder player){
        System.out.println("ChunkService.addPlayer");
    }

    public void removePlayer(String chunkID, String playerID){
        System.out.println("ChunkService.removePlayer");
    }

    public void updatePlayer(Player.Builder player){
        System.out.println("ChunkService.updatePlayer");
    }

    protected Chunk.Builder newChunk(float x, float y, float size, String parentChunk){
        return Chunk.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setPosition(Vector.newBuilder().setX(x).setY(y))
                .setSize(Vector.newBuilder().setX(size).setY(size))
                .setParentChunk(parentChunk);
    }

    protected ChunkInfo.Builder chunkToInfo(Chunk.Builder chunk){
        return ChunkInfo.newBuilder()
                .setId(chunk.getId())
                .setPosition(chunk.getPosition())
                .setSize(chunk.getSize())
                .setParentChunk(chunk.getParentChunk())
                .addAllChildChunks(chunk.getChildChunksList());
    }

    protected Chunk.Builder findChunk(Vector position){
        System.out.println("ChunkService.findChunk");
        return null;
    }

    protected boolean inChunk(Chunk.Builder chunk, Vector point){
        return Math.abs(chunk.getPosition().getX() - point.getX()) <= chunk.getSize().getX()/2
                && Math.abs(chunk.getPosition().getY() - point.getY()) <= chunk.getSize().getY()/2;
    }

    public void close(){
        changeFile.close();
        chunkFile.close();
    }
}
