package strategies;
import strategies.service.*;

public class App {
    private static GameService gameService;

    /*public String getGreeting() {
        return "Hello World!";
    }*/

    public static void main(String[] args) {
        //System.out.println(new App().getGreeting());

        gameService = new GameService();
        gameService.createGame(10, 10, 10, 1);
    }
}
