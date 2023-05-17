package strategies;
import strategies.service.*;

public class App {
    private static GameService gameService;

    //public String getGreeting() { return "Hello World!"; }

    public static void main(String[] args) {
        //System.out.println(new App().getGreeting());

        // Arguments: botCount, obstacleCount, itemCount, seed, verbose
        if(args.length < 5) {
            System.out.println("Too few command line arguments!");
            return;
        }

        System.out.println("Create new game");
        gameService = new GameService();
        gameService.createGame(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]),
                Integer.parseInt(args[3]), Boolean.parseBoolean(args[4]));

        gameService.printGame();
    }
}
