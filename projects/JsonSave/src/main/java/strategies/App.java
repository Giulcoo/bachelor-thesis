package strategies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import strategies.model.Change;
import strategies.model.Chunk;
import strategies.model.Vector;
import strategies.service.ChangeFileService;
import strategies.service.FileManager;
import strategies.service.GameService;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {
//        FileManager.deleteData();
//        FileManager.createFolders();
//        FileManager.createFile(Constants.CHANGE_FILE);
//
//        ChangeFileService service = new ChangeFileService();
//
//        Chunk chunk1 = new Chunk()
//                .setId("1")
//                .setPosition(new Vector(1,2))
//                .setSize(new Vector(3,4));
//
//        Chunk chunk2 = new Chunk()
//                .setId("2")
//                .setPosition(new Vector(1,2))
//                .setSize(new Vector(3,4));
//
//        Chunk chunk3 = new Chunk()
//                .setId("3")
//                .setPosition(new Vector(1,2))
//                .setSize(new Vector(3,4));
//
//        service.saveChunkAdded(chunk1);
//        service.saveChunkAdded(chunk2);
//        service.saveChunkAdded(chunk3);
//
//        FileManager.readChanges().forEach(n -> {
//            try {
//                Chunk c = new ObjectMapper().treeToValue(n.get("value"), Chunk.class);
//                System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(c));
//            }
//            catch (JsonProcessingException e){
//                System.out.println("Could not get chunk");
//            }
//        });
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