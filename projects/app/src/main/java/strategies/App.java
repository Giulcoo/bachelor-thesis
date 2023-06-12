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
        boolean loading = true;

        if(loading){
            //new CompressionService().decompressData();

            gameService.createGame(seed, verbose);
            loadService.loadData(seed, verbose);
            loadService.loadChunksNearPlayer();
            chunkService.printChunks();
        }
        else{
            saveService.clearData();

            saveService.createFolderStructure();
            gameService.createGame(obstacleCount, botCount, itemCount, seed, verbose);

            saveService.saveAsJson();
        }

        /*for(int i = 0; i < 3; i++){
            gameService.randomChanges(i * 10, i * 10, i * 5, i * 5, i * 10, i * 10);
            saveService.saveAsJson();
        }

        new CompressionService().compressData();*/
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
