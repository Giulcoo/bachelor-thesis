package strategies;
import strategies.service.*;

public class App {
    private static final GameService gameService = new GameService();
    private static final ChangeTracker changeTracker = new ChangeTracker();
    private static final TimeService timeService = new TimeService();

    public static void main(String[] args) {
        if(args.length < 5) {
            System.out.println("Arguments needed: botCount (int), obstacleCount (int), itemCount (int), seed (int), verbose (boolean)");
            return;
        }

        //TODO: Load or create new game
        timeService.start("createGame");
        gameService.createGame(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]),
                Integer.parseInt(args[3]), Boolean.parseBoolean(args[4]));
        timeService.stop("createGame");

        gameService.randomChanges(10, 10, 5, 5, 10, 10);

        //TODO: Save game every x seconds or everytime something changes
        //TODO: Save game when program is closed
    }
}
