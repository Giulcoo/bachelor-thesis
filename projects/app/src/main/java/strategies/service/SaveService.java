package strategies.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import strategies.model.Character;

import java.util.List;

public class SaveService {
    private final ObjectMapper mapper = new ObjectMapper();
    private final GameService gameService;
    private final ChangeTracker changeTracker;

    public SaveService(GameService gameService, ChangeTracker changeTracker) {
        this.gameService = gameService;
        this.changeTracker = changeTracker;
    }

    public void saveAsJson(){
        List<Character> changes = changeTracker.getChangedCharacters();
        System.out.println(changes.get(0).toString());

        try {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(changes.get(0)));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
