package strategies.service;

import strategies.Constants;
import strategies.model.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class GameService {
    private final SaveService saveService;
    private final ChunkService chunkService;
    private final Game.Builder game;
    private final boolean dynamicChunkSize;
    private final boolean useChangeFile;

    public GameService(boolean dynamicChunkSize, boolean useChangeFile) {
        this.saveService = new SaveService();
        this.game = Game.newBuilder();
        this.chunkService = new ChunkService(saveService, game, dynamicChunkSize, useChangeFile);
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
        if(!useChangeFile){
            saveService.saveChanges();
        }
    }

    public void loadGame(){
        //TODO: Load game depending on useChangeFile
    }

    public void close(){
        saveService.close();
    }
}
