package strategies.old.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import strategies.old.model.*;
import strategies.old.model.Character;
import strategies.old.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static strategies.old.Constants.*;

public class LoadService {

    private final GameService gameService;
    private final ChunkService chunkService;

    private final ObjectMapper mapper = new ObjectMapper();

    private final HashMap<String, List<String>> childrenIDs = new HashMap<>();
    private final HashMap<String, Chunk> chunks = new HashMap<>();
    private Chunk<Character> charRootChunk;
    private Chunk<Item> itemRootChunk;
    private Chunk<Obstacle> obstRootChunk;

    private final List<Character> characters = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private final List<Obstacle> obstacles = new ArrayList<>();

    private Vector playerPos;

    public LoadService(GameService gameService, ChunkService chunkService) {
        this.gameService = gameService;
        this.chunkService = chunkService;
    }

    public void loadData(int seed, boolean verbose){
        getGameData();
        List<Chunk<Character>> characterChunks = getChunks("characters");
        List<Chunk<Item>> itemChunks = getChunks("items");
        List<Chunk<Obstacle>> obstacleChunks = getChunks("obstacles");

        linkChunks(charRootChunk);
        linkChunks(itemRootChunk);
        linkChunks(obstRootChunk);

        chunkService.clearChunks();
        chunkService.addChunks(characterChunks, itemChunks, obstacleChunks);
    }

    public void loadChunksNearPlayer(){
        Character player = gameService.getGame().getPlayer();

        if(player == null && playerPos == null) return;

        if(player != null) this.playerPos = player.getPosition();
        
        chunkService.getRootCharacterChunk()
                .chunksInArea(playerPos, RENDER_DISTANCE)
                .stream().filter(c -> c.getElements().isEmpty()).toList()
                .forEach(c -> getChunkElements("characters/" + c.getElementId() + ".json", c));

        chunkService.getRootItemChunk()
                .chunksInArea(playerPos, RENDER_DISTANCE)
                .stream().filter(c -> c.getElements().isEmpty()).toList()
                .forEach(c -> getChunkElements("items/" + c.getElementId() + ".json", c));

        chunkService.getRootObstacleChunk()
                .chunksInArea(playerPos, RENDER_DISTANCE)
                .stream().filter(c -> c.getElements().isEmpty()).toList()
                .forEach(c -> getChunkElements("obstacles/" + c.getElementId() + ".json", c));
    }

    private <T> void linkChunks(Chunk<T> chunk){
        childrenIDs.get(chunk.getId()).forEach(id -> {
            Chunk<T> childChunk = chunks.get(id);
            chunk.addChild(childChunk);
            childChunk.setParent(chunk);
            linkChunks(childChunk);
        });
    }

    public void getGameData(){
        try{
            JsonNode node = mapper.readTree(new File(DATA_PATH + "game.json"));

            playerPos = new Vector(node.get("playerPosition"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private <T> List<Chunk<T>> getChunks(String path){
        List<Chunk<T>> result = new ArrayList<>();

        for(File file : new File(DATA_PATH + path).listFiles()){
            if (file.isFile() && file.getName().charAt(1) == '-') {
                Chunk<T> chunk = getChunk(path + "/" + file.getName());

                if(chunk == charRootChunk || chunk == itemRootChunk || chunk == obstRootChunk){
                    result.add(0, chunk);
                }
                else{
                    result.add(chunk);
                }
            }
        }

        return result;
    }

    private <T> Chunk<T> getChunk(String path){
        Chunk<T> result = null;

        try{
            JsonNode node = mapper.readTree(new File(DATA_PATH + path));

            String id = node.get("id").asText();
            Vector center = new Vector(node.get("center"));
            Vector size = new Vector(node.get("size"));

            result = new Chunk<>(id, center, size);

            List<String> childrenIds = mapper.convertValue(node.get("childChunksIds"), new TypeReference<>() {});
            childrenIDs.put(id, childrenIds);
            chunks.put(id, result);

            if(size.getX() == MAP_SIZE){
                if(id.charAt(0) == CHARACTER_TYPE) charRootChunk = (Chunk<Character>) result;
                else if(id.charAt(0) == ITEM_TYPE) itemRootChunk = (Chunk<Item>) result;
                else if(id.charAt(0) == OBSTACLE_TYPE) obstRootChunk = (Chunk<Obstacle>) result;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    private <T> void getChunkElements(String path, Chunk<T> chunk){
        try{
            System.out.println(chunk.getId());
            JsonNode node = mapper.readTree(new File(DATA_PATH + path));
            List<T> elements = mapper.convertValue(node, new TypeReference<>() {});
            chunk.addElements(elements);

            if(!elements.isEmpty()){
                if(elements.get(0) instanceof Character) gameService.addCharacters((List<Character>) elements);
                else if(elements.get(0) instanceof Item) gameService.addItems((List<Item>) elements);
                else if(elements.get(0) instanceof Obstacle) gameService.addObstacles((List<Obstacle>) elements);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
