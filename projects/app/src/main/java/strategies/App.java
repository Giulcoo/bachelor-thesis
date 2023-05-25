package strategies;
import com.fasterxml.jackson.databind.ObjectMapper;
import strategies.model.Character;
import strategies.service.*;

import java.util.List;

public class App {
    private static final TimeService timeService = new TimeService();
    private static final ChangeTracker changeTracker = new ChangeTracker();
    private static final GameService gameService = new GameService(changeTracker);

    private static final SaveService saveService = new SaveService(gameService, changeTracker);

    public static void main(String[] args) {
        if(args.length < 5) {
            System.out.println("Arguments needed: botCount (int), obstacleCount (int), itemCount (int), seed (int), verbose (boolean)");
            return;
        }

       startGame(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]),
               Integer.parseInt(args[3]), Boolean.parseBoolean(args[4]));
    }

    public static void startGame( int obstacleCount, int botCount, int itemCount, int seed, boolean verbose){
        //TODO: Load or create new game
        //if(saveService.createFolderStructure()){
            timeService.start("createGame");
            gameService.createGame(obstacleCount, botCount, itemCount, seed, verbose);
            timeService.stop("createGame");
        //}

        gameService.randomChanges(10, 10, 5, 5, 10, 10);

        //TODO: Save game every x seconds or everytime something changes
        //TODO: Save game when program is closed
        saveService.saveAsJson();
    }
}
