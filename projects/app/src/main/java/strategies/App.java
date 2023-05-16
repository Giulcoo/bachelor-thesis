package strategies;
import strategies.service.*;

public class App {
    private static GameService gameService;

    //public String getGreeting() { return "Hello World!"; }

    public static void main(String[] args) {
        //System.out.println(new App().getGreeting());

        System.out.println("Create new game");
        gameService = new GameService();
        gameService.createGame(10, 10, 10, 1);

        gameService.printGame();
    }
}
