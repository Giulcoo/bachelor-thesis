package strategies;

import strategies.service.GameService;

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