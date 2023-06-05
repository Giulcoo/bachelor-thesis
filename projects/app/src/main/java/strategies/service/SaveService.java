package strategies.service;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import strategies.model.Character;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.List;

public class SaveService {
    private String mainPath;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
    private final GameService gameService;
    private final ChangeTracker changeTracker;

    public SaveService(GameService gameService, ChangeTracker changeTracker) {
        this.gameService = gameService;
        this.changeTracker = changeTracker;

        mainPath = null;
        try {
            mainPath = Paths.get(getClass().getResource("/").toURI()).toString();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        if(mainPath != null) mainPath += "/resources/strategies/";
    }

    public boolean createFolderStructure(){
        return createFileIfNeeded("", "game.json")
                && createFileIfNeeded("characters", "characters.json")
                && createFileIfNeeded("items", "items.json")
                && createFileIfNeeded("obstacles", "obstacles.json");
    }

    public void saveAsJson(){
        writeJson("obstacles/obstacles.json", changeTracker.getAddedObstacles());
        writeJson("characters/characters.json", changeTracker.getChangedCharacters());
        writeJson("items/items.json", changeTracker.getChangedItems());
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
