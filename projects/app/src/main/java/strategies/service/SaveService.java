package strategies.service;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import strategies.model.Character;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SaveService {
    private final String mainPath = "data/";
    private final GameService gameService;
    private final ChangeTracker changeTracker;

    //JSON Objects
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
    private final ArrayNode characterNode;
    public SaveService(GameService gameService, ChangeTracker changeTracker) {
        this.gameService = gameService;
        this.changeTracker = changeTracker;

        characterNode = mapper.createArrayNode();
    }

    public boolean createFolderStructure(){
        return createFileIfNeeded("", "game.json")
                && createDirIfNeeded("characters")
                && createDirIfNeeded("items")
                && createDirIfNeeded("obstacles");
    }

    public void saveAsJson(){
        changeTracker.getCharacterChanges().forEach(c -> writeJson("characters/" + c.getId() + ".json", c));
        changeTracker.getItemChanges().forEach(c -> writeJson("items/" + c.getId() + ".json", c));
        changeTracker.getObstacleChange().forEach(c -> writeJson("obstacles/" + c.getId() + ".json", c));

        changeTracker.getRemovedCharacterChunks().forEach(c -> deleteFile("characters/" + c.getId() + ".json"));
        changeTracker.getRemovedItemChunks().forEach(c -> deleteFile("items/" + c.getId() + ".json"));
    }

    public void clearData(){
        try {
            deleteDirectoryRecursive(Paths.get(mainPath));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void writeJson(String file, Object object){
        try {
            writer.writeValue(new File(mainPath + file), object);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteFile(String path){
        try {
            Files.delete(Paths.get(mainPath, path));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteDirectoryRecursive(Path path) throws IOException {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    deleteDirectoryRecursive(entry);
                }
            }
        }
        Files.delete(path);
    }

    private boolean createDirIfNeeded(String path){
        try {
            Path dirPath = Paths.get(mainPath, path);

            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                return true;
            }
        }
        catch (Exception e) {
            System.out.println("Could not create directory: " + path);
        }

        return false;
    }

    private boolean createFileIfNeeded(String path, String file){
        createDirIfNeeded(path);

        try {
            Path dirPath = Paths.get(mainPath, path + "/" + file);

            if (!Files.exists(dirPath)) {
                Files.createFile(dirPath);
                return true;
            }
        }
        catch (Exception e) {
            System.out.println("Could not create directory: " + path);
        }

        return false;
    }
}
