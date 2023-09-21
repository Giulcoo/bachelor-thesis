package strategies.service;

import strategies.Constants;
import strategies.model.Player;
import strategies.model.Vector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import static strategies.Constants.*;

public class GameService {
    private final ChunkService chunkService;
    private final Random random;
    private Player.Builder player;
    private final List<Player.Builder> bots = new ArrayList<>();

    public GameService() {
        this.random = new Random(SEED);

        this.chunkService = DYNAMIC_CHUNK_SIZE? new DynamicChunkService() : new StaticChunkService();
    }

    public void createGame(int dataCount){
        FileManager.resetFolders();
        chunkService.createChunks();
        chunkService.addPlayer(generatePlayer(false));
        randomNewPlayers(dataCount-1);
    }

    public void saveGame(){
        chunkService.saveChunks();
    }

    public void loadGame(){
        player = chunkService.loadChunks();
    }

    public void printGame(){
        chunkService.printGame();
    }

    public void randomNewPlayers(int count){
        IntStream.range(0, count).forEach(i -> chunkService.addPlayer(generateBotNearPlayer()));
    }

    public void randomDeleteBot(int count){
        IntStream.range(0, count).forEach(i-> randomDeleteBot());
    }

    /**Moves player once and count-1 amount of random bots randomly*/
    public void randomMovePlayer(int count){
        if(count > 1 && bots.isEmpty()) randomNewPlayers(1);

        IntStream.range(0, count).forEach(i-> randomMovePlayer(i > 0));
    }

    private Player.Builder generatePlayer(boolean isBot){
        Player.Builder newPlayer = Player.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setName(isBot ? "Monster" : "Player")
                .setIsBot(isBot)
                .setPosition(randVector());

        if(isBot){
            bots.add(newPlayer);
        }
        else{
            player = newPlayer;
        }

        return newPlayer;
    }

    private Player.Builder generateBotNearPlayer(){
        Player.Builder newPlayer = Player.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setName("Monster")
                .setIsBot(true)
                .setPosition(randVector(this.player.getPosition(), BOT_SPAWN_DISTANCE_FROM_PLAYER));

        bots.add(newPlayer);

        return newPlayer;
    }

    private void randomDeleteBot(){
        int randBotIndex = randInt(0,bots.size()-1);
        Player.Builder randPlayer = bots.get(randBotIndex);

        chunkService.removePlayer(randPlayer);
        bots.remove(randBotIndex);
    }

    private void randomMovePlayer(boolean bot){
        Player.Builder player = bot? randBot() : this.player;
        player.setPosition(randVector(player.getPosition(), PLAYER_MOVE_MAGNITUDE));
        chunkService.updatePlayer(player);
    }

    private Player.Builder randBot(){
        return bots.get(randInt(0,bots.size()-1));
    }

    private Vector.Builder checkInMapBounds(Vector.Builder vector){
        if(vector.getX() < 0) vector.setX(0);
        if(vector.getY() < 0) vector.setY(0);
        if(vector.getX() > MAP_SIZE) vector.setX(MAP_SIZE);
        if(vector.getY() > MAP_SIZE) vector.setY(MAP_SIZE);
        return vector;
    }

    private Vector.Builder randVector(){
        return checkInMapBounds(Vector.newBuilder().setX(randFloat()).setY(randFloat()));
    }

    private Vector.Builder randVector(float magnitude){
        //Create random Vector
        Vector.Builder randVector = randVector();

        //Normalize Vector and set magnitude
        double norm = Math.sqrt(randVector.getX() * randVector.getX() + randVector.getY() * randVector.getY());
        randVector.setX((float) ((randVector.getX() / norm) * magnitude));
        randVector.setY((float) ((randVector.getY() / norm) * magnitude));

        return checkInMapBounds(randVector);
    }

    private Vector.Builder randVector(Vector center, float magnitude) {
       Vector.Builder randVector = randVector(magnitude);

        return checkInMapBounds(Vector.newBuilder()
                .setX(center.getX() + randVector.getX())
                .setY(center.getY() + randVector.getY()));
    }

    private float randFloat(){
        return random.nextFloat() * MAP_SIZE;
    }

    private float randFloat(float min, float max){
        return min + random.nextFloat() * (max - min);
    }

    private int randInt(int min, int max){
        return random.nextInt((max - min) + 1) + min;
    }

    public void close(){
        chunkService.close();
    }
}
