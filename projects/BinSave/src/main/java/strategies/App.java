package strategies;

import strategies.service.GameService;

import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {
        createGame(100000);
        //createGameAndPlay(10, 10, 10, 10, 5);
        //loadGame();
        //loadGameAndPlay(10, 10, 10, 10);
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

    public static void createGameAndPlay(int initCount, int moves, int moveChanges, int botAdd, int botRemove){
        GameService service = new GameService();
        service.createGame(initCount);
        service.saveGame();

        IntStream.range(0, moves).forEach(i -> {
            service.randomNewPlayers(botAdd);
            service.randomDeleteBot(botRemove);
            service.randomMovePlayer(moveChanges);
            service.saveGame();
        });

        service.printGame();

        service.close();
    }

    public static void loadGameAndPlay(int moves, int moveChanges, int botAmountChanges){
        GameService service = new GameService();
        service.loadGame();

        IntStream.range(0, moves).forEach(i -> {
            service.randomNewPlayers(botAmountChanges);
            service.randomDeleteBot(botAmountChanges);
            service.randomMovePlayer(moveChanges);
        });

        service.close();
    }
}