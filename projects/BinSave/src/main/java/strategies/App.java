package strategies;

import strategies.model.Chunk;
import strategies.model.Player;
import strategies.model.Vector;
import strategies.service.GameService;

import java.io.*;
import java.util.stream.IntStream;

import static strategies.Constants.*;

public class App {
    public static void main(String[] args) {
        //createGame(2);
        loadGame();
        //createGameAndPlay(10, 10, 10, 10);
    }

    public static void createGame(int dataCount){
        GameService service = new GameService();
        service.createGame(dataCount);
        service.saveGame();
        service.saveGame();
        service.saveGame();
        service.close();
    }

    public static void loadGame(){
        GameService service = new GameService();
        service.loadGame();
        service.close();
    }

    public static void createGameAndPlay(int initCount, int moves, int moveChanges, int botAmountChanges){
        GameService service = new GameService();
        service.createGame(initCount);
        service.saveGame();

        IntStream.range(0, moves).forEach(i -> {
            service.randomDeleteBot(botAmountChanges);
            service.randomNewPlayers(botAmountChanges);
            service.randomMovePlayer(moveChanges);
        });

        service.close();
    }

    public static void loadGameAndPlay(int initCount, int moves, int moveChanges, int botAmountChanges){
        GameService service = new GameService();
        service.loadGame();

        IntStream.range(0, moves).forEach(i -> {
            service.randomDeleteBot(botAmountChanges);
            service.randomNewPlayers(botAmountChanges);
            service.randomMovePlayer(moveChanges);
        });

        service.close();
    }
}