package strategies.service;

import strategies.Constants;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static strategies.Constants.*;

public class FileManager {
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
}
