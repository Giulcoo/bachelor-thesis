package strategies.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import strategies.Constants;
import strategies.model.Chunk;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static strategies.Constants.*;

public class FileManager {
    private final static ObjectMapper mapper = new ObjectMapper();

    public static void resetFolders(){
        deleteData();
        createFolders();
    }

    public static void createFolders(){
        //Create data folder, if it does not exist
        try {
            Path dataPath = Path.of(Constants.DATA_PATH);
            if(Files.notExists(dataPath)) {
                Files.createDirectory(dataPath);
            }

            if(Files.notExists(CHUNK_FOLDER)){
                Files.createDirectory(CHUNK_FOLDER);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void deleteData(){
        try{
            delete(new File(DATA_PATH));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }

        f.delete();
    }

    public static void clearFile(String path){
        deleteFile(path);
        createFile(path);
    }

    public static void tryClose(Closeable closeable){
        try {
            if (closeable != null) closeable.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void deleteFile(String path){
        File file = new File(path);
        if(file.exists()) file.delete();
    }

    public static void createFile(String path){
        try{
            File newFile = new File(path);
            newFile.createNewFile();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void writeToFile(String path, Object object){
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), object);
        }
        catch (IOException e) {
            System.out.println("FileManager::writeToFile -> Could not serialize object");
        }
    }

    public static void appendChanges(Object object){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(CHANGE_FILE, true))){
            writer.write(mapper.writeValueAsString(object));
            writer.newLine();
        }
        catch (IOException e){
            System.out.println("FileManager::appendChanges -> Could not write line " + CHANGE_FILE);
        }
    }

    public static <T> T readFile(String path, Class<T> clazz){
        try{
            return mapper.readValue(new File(path), clazz);
        }
        catch (IOException e){
            System.out.println("FileManager::readFile -> Could not read file " + path);
        }

        return null;
    }

    public static List<JsonNode> readChanges(){
        List<JsonNode> changes = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CHANGE_FILE)))){
            while(reader.ready()){
                changes.add(mapper.readTree(reader.readLine()));
            }
        }
        catch(FileNotFoundException e){
            System.out.println("FileManager::readChanges -> Could not find change file " + CHANGE_FILE);
        }
        catch(IOException e){
            System.out.println("FileManager::readChanges -> Could not read lines of " + CHANGE_FILE);
        }

        clearFile(Constants.CHANGE_FILE);

        return changes;
    }
}
