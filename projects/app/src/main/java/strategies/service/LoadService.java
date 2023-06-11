package strategies.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import strategies.Constants;
import strategies.model.*;
import strategies.model.Character;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static strategies.Constants.*;

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

    public LoadService(GameService gameService, ChunkService chunkService) {
        this.gameService = gameService;
        this.chunkService = chunkService;
    }

    public void loadData(int seed, boolean verbose){
        List<Chunk<Character>> characterChunks = getChunks("characters");
        List<Chunk<Item>> itemChunks = getChunks("items");
        List<Chunk<Obstacle>> obstacleChunks = getChunks("obstacles");

        linkChunks(charRootChunk);
        linkChunks(itemRootChunk);
        linkChunks(obstRootChunk);

        chunkService.clearChunks();
        chunkService.addChunks(characterChunks, itemChunks, obstacleChunks);
        gameService.loadGame(obstacles, characters, items, seed, verbose);
    }

    private <T> void linkChunks(Chunk<T> chunk){
        childrenIDs.get(chunk.getId()).forEach(id -> {
            Chunk<T> childChunk = chunks.get(id);
            chunk.addChild(childChunk);
            childChunk.setParent(chunk);
            linkChunks(childChunk);
        });
    }

    private <T> List<Chunk<T>> getChunks(String path){
        List<Chunk<T>> result = new ArrayList<>();

        for(File file : new File(MAIN_PATH + path).listFiles()){
            if (file.isFile()) {
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
            JsonNode node = mapper.readTree(new File(MAIN_PATH + path));

            String id = node.get("id").asText();
            Vector center = new Vector(node.get("center"));
            Vector size = new Vector(node.get("size"));
            List<T> elements = mapper.convertValue(node.get("elements"), new TypeReference<>() {});

            if(!elements.isEmpty()){
                if(elements.get(0) instanceof Character) characters.addAll((List<Character>) elements);
                else if(elements.get(0) instanceof Item) items.addAll((List<Item>) elements);
                else if(elements.get(0) instanceof Obstacle) obstacles.addAll((List<Obstacle>) elements);
            }

            result = new Chunk<>(id, center, size, elements);

            List<String> childrenIds = mapper.convertValue(node.get("childChunksIds"), new TypeReference<>() {});
            childrenIDs.put(id, childrenIds);
            chunks.put(id, result);

            if(size.getX() == MAP_SIZE){
                System.out.println(id + " " + childrenIds);
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
}
