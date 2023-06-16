package strategies;
import strategies.service.*;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class App {
    private static final TimeService timeService = new TimeService();
    private static final ChangeTracker changeTracker = new ChangeTracker();
    private static final ChunkService chunkService = new ChunkService(changeTracker);
    private static final GameService gameService = new GameService(changeTracker, chunkService);
    private static final SaveService saveService = new SaveService(gameService, changeTracker);
    private static final LoadService loadService = new LoadService(gameService, chunkService);

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        if(args.length < 5) {
            System.out.println("Arguments needed: botCount (int), obstacleCount (int), itemCount (int), seed (int), verbose (boolean)");
            return;
        }

       startGame(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]),
               Integer.parseInt(args[3]), Boolean.parseBoolean(args[4]));
    }

    public static void startGame( int obstacleCount, int botCount, int itemCount, int seed, boolean verbose){
        boolean loading = false;

        if(loading){
            loadGame(seed, verbose);
        }
        else{
            new MongoService();
            createGame(obstacleCount, botCount, itemCount, seed, verbose);
        }
    }

    public static void loadGame(int seed, boolean verbose){
        gameService.createGame(seed, verbose);
        loadService.loadData(seed, verbose);
        loadService.loadChunksNearPlayer();
    }

    public static void createGame(int obstacleCount, int botCount, int itemCount, int seed, boolean verbose){
        saveService.clearData();

        saveService.createFolderStructure();
        gameService.createGame(obstacleCount, botCount, itemCount, seed, verbose);

        saveService.saveAsJson();
    }

    public static void changes(int amount){
        for(int i = 0; i < amount; i++){
            gameService.randomChanges(10, 10, 5, 5, 10, 10);
            loadService.loadChunksNearPlayer();
            saveService.saveAsJson();
        }
    }

    private static void sleep(int seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void breakPoint(){
        System.out.print("\n[breakpoint]");
        scanner.nextLine();
    }
}
