package strategies;

import strategies.service.GameService;

public class App {
    public static void main(String[] args) {
        createGame(100);
        removePlayers(10);
    }

    public static void createGame(int dataCount){
        GameService service = new GameService();
        service.createGame(dataCount);
        service.saveGame();
        service.printGame();
    }

    public static void loadGame(){
        GameService service = new GameService();
        service.loadGame();
        service.printGame();
    }

    public static void createPlayers(int dataCount){
        GameService service = new GameService();
        service.loadGame();

        service.randomNewPlayers(dataCount);

        service.printGame();
    }

    public static void movePlayers(int dataCount){
        GameService service = new GameService();
        service.loadGame();

        service.randomMovePlayer(dataCount);

        service.printGame();
    }

    public static void removePlayers(int dataCount){
        GameService service = new GameService();
        service.loadGame();

        service.randomDeleteBot(dataCount);

        service.printGame();
    }
}