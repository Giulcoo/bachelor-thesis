package strategies;

import strategies.service.GameService;

public class App {
    public static void main(String[] args) {
        createGame(1010);
        movePlayers(1000);
    }

    public static void createGame(int dataCount){
        GameService service = new GameService();
        service.createGame(dataCount);
        service.saveGame();
        service.printGame();
        service.close();
    }

    public static void loadGame(){
        GameService service = new GameService();
        service.loadGame();
        service.printGame();
        service.close();
    }

    public static void createPlayers(int dataCount){
        GameService service = new GameService();
        service.loadGame();

        service.randomNewPlayers(dataCount);

        service.printGame();
        service.close();
    }

    public static void movePlayers(int dataCount){
        GameService service = new GameService();
        service.loadGame();

        service.randomMovePlayer(dataCount);

        service.printGame();
        service.close();
    }

    public static void removePlayers(int dataCount){
        GameService service = new GameService();
        service.loadGame();
        service.printGame();

        service.randomDeleteBot(dataCount);

        service.printGame();
        service.close();
    }
}