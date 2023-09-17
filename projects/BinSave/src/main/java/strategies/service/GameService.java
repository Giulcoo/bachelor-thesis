package strategies.service;

import strategies.model.Game;
import strategies.model.Player;
import strategies.model.Vector;

import java.util.UUID;

public class GameService {
    private final ChunkService chunkService;
    private final Game.Builder game;
    private final boolean dynamicChunkSize;
    private final boolean useChangeFile;

    public GameService(boolean dynamicChunkSize, boolean useChangeFile) {
        this.game = Game.newBuilder();
        //this.chunkService = new ChunkService(game, dynamicChunkSize, useChangeFile);
        this.chunkService = new StaticChunkService(game, useChangeFile);

        this.dynamicChunkSize = dynamicChunkSize;
        this.useChangeFile = useChangeFile;
    }

    public void createGame(int dataCount){
        //TODO: Create Chunks in ChunkService
    }

    public void randomChanges(int count){
        //TODO: Random add, update and remove
    }

    public Player.Builder generatePlayer(boolean isBot){
        return Player.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setName(isBot ? "Player" : "Monster")
                .setIsBot(isBot)
                .setPosition(Vector.newBuilder().setX(0).setY(0).build());
    }

    public void saveGame(){
        chunkService.saveChunks();
    }

    public void loadGame(){
        chunkService.loadChunks();
    }

    public void close(){
        chunkService.close();
    }
}
