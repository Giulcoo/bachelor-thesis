package strategies;

import strategies.service.GameService;

public class App {
    public static void main(String[] args) {
        createGame(10);
    }

    public static void createGame(int dataCount){
        GameService service = new GameService(false, false);
        service.createGame(dataCount);
        service.saveGame();
        service.close();
    }

//    public static void protoTest(){
//        Change.Builder outputChange1 = Change.newBuilder()
//                .setId("1")
//                .setType(Change.Type.CHUNK)
//                .setEvent(Change.Event.UPDATED)
//                .setKey("position")
//                .setValue(Any.pack(Vector.newBuilder().setX(1).setY(2).build()));
//
//        Change.Builder outputChange2 = Change.newBuilder()
//                .setId("2")
//                .setType(Change.Type.PLAYER)
//                .setEvent(Change.Event.ADDED)
//                .setValue(Any.pack(Player.newBuilder()
//                        .setId("1")
//                        .setName("Player")
//                        .setIsBot(false)
//                        .setChunk("1")
//                        .setPosition(Vector.newBuilder().setX(0).setY(0))
//                        .build()
//                ));
//
//        SaveService service = new SaveService();
//
//        service.saveChange(outputChange1);
//        service.saveChange(outputChange2);
//
//        List<Change> changes = service.getChanges();
//        System.out.println(changes.size());
//
//        System.out.println(changes.get(0).getId());
//
//        try{
//            Vector testVector = changes.get(0).getValue().unpack(Vector.class);
//            System.out.println(testVector.getX() +  " " + testVector.getY());
//
//            Player testPlayer = changes.get(1).getValue().unpack(Player.class);
//            System.out.println(testPlayer.getName());
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//
//        service.close();
//    }
}