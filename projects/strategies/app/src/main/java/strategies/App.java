package strategies;

import strategies.service.ProtoService;

public class App {
    public static void main(String[] args) {
        binScenario(10);
    }

    public static void binScenario(int dataCount){
        ProtoService service = new ProtoService();
        service.createGame(dataCount);
        service.saveGame();
        service.close();
    }

    public static void jsonScenario(int dataCount){

    }
}
