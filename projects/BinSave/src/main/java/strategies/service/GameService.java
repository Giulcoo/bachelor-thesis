package strategies.service;

import strategies.model.Player;
import strategies.model.Vector;

import java.util.*;
import java.util.stream.IntStream;

import static strategies.Constants.*;

public class GameService {
    private final ChunkService chunkService;
    private final Random random;
    private Player.Builder player;
    private final Map<String, Player.Builder> bots = new HashMap<>();

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
        chunkService.getBots().forEach(p -> bots.put(p.getId(), p));
    }

    public void printGame(){
        chunkService.printGame();
    }

    public void randomNewPlayers(int count){
        IntStream.range(0, count).forEach(i -> {
            Player.Builder bot = generatePlayer(true);
            bots.put(bot.getId(), chunkService.addPlayer(bot));
        });
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
                .setPosition(isBot ? randVector(this.player.getPosition(), BOT_SPAWN_DISTANCE_FROM_PLAYER) : randVector());

        if(isBot){
            bots.put(newPlayer.getId(), newPlayer);
        }
        else{
            player = newPlayer;
        }

        return newPlayer;
    }


    // ------------------
    // Random functions |
    // ------------------
    private void randomDeleteBot(){
        Player.Builder randPlayer = randBot();

        chunkService.removePlayer(randPlayer);
        bots.remove(randPlayer.getId());
    }

    private void randomMovePlayer(boolean bot){
        Player.Builder player = bot? randBot() : this.player;
        player.setPosition(randVector(player.getPosition(), PLAYER_MOVE_MAGNITUDE));

        if(bot){
            bots.put(player.getId(), chunkService.updatePlayer(player));
        }
        else{
            this.player = chunkService.updatePlayer(player);
        }
    }

    private Player.Builder randBot(){
        return bots.values().stream().toList().get(randInt(0,bots.size()-1));
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

    private int randInt(int min, int max){
        return random.nextInt((max - min) + 1) + min;
    }

    public void close(){
        chunkService.close();
    }
}
