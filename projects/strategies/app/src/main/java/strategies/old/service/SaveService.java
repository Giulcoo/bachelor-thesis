package strategies.old.service;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.*;
import java.nio.file.*;

import static strategies.old.Constants.CHANGES_FILE;
import static strategies.old.Constants.DATA_PATH;

public class SaveService {
    private final GameService gameService;
    private final ChangeTracker changeTracker;

    //JSON Objects
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

    public SaveService(GameService gameService, ChangeTracker changeTracker) {
        this.gameService = gameService;
        this.changeTracker = changeTracker;
    }

    public boolean createFolderStructure(){
        return createFileIfNeeded("", "game.json")
                && createDirIfNeeded("characters")
                && createDirIfNeeded("items")
                && createDirIfNeeded("obstacles");
    }

    public void saveAsJson(){
        changeTracker.getCharacterChanges().forEach(c -> {
            writeJson("characters/" + c.getId() + ".json", c);
            writeJson("characters/" + c.getElementId() + ".json", c.getElements());
        });

        changeTracker.getItemChanges().forEach(c -> {
            writeJson("items/" + c.getId() + ".json", c);
            writeJson("items/" + c.getElementId() + ".json", c.getElements());
        });

        changeTracker.getObstacleChange().forEach(c -> {
            writeJson("obstacles/" + c.getId() + ".json", c);
            writeJson("obstacles/" + c.getElementId() + ".json", c.getElements());
        });

        changeTracker.getRemovedCharacterChunks().forEach(c -> {
            deleteFile("characters/" + c.getId() + ".json");
            deleteFile("characters/" + c.getElementId() + ".json");
        });

        changeTracker.getRemovedItemChunks().forEach(c -> {
            deleteFile("items/" + c.getId() + ".json");
            deleteFile("items/" + c.getElementId() + ".json");
        });

        writeJson("game.json", gameService.getGame());
    }

    public void saveChanges() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(CHANGES_FILE));
            PrintWriter printWriter = new PrintWriter(writer);
            ArrayNode changes = mapper.createArrayNode();

            changes.add("Changed");
            changeTracker.getCharacterChanges().forEach(c -> changes.addPOJO(c.getElements()));
            changeTracker.getItemChanges().forEach(c -> changes.addPOJO(c.getElements()));
            changeTracker.getObstacleChange().forEach(c -> changes.addPOJO(c.getElements()));

            int size = changes.size();
            changes.add("Removed");
            changeTracker.getRemovedCharacterChunks().forEach(c -> changes.addPOJO(c.getElements()));
            changeTracker.getRemovedItemChunks().forEach(c -> changes.addPOJO(c.getElements()));

            //f(size != changes.size()) changes.insert(size, "Removed");

            System.out.println(changes.get(size-1));
            printWriter.println(changes.toPrettyString());
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearData(){
        try {
            deleteDirectoryRecursive(Paths.get(DATA_PATH));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void writeJson(String file, Object object){
        try {
            writer.writeValue(new File(DATA_PATH + file), object);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteFile(String path){
        try {
            Files.delete(Paths.get(DATA_PATH, path));
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
            Path dirPath = Paths.get(DATA_PATH, path);

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
            Path dirPath = Paths.get(DATA_PATH, path + "/" + file);

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
