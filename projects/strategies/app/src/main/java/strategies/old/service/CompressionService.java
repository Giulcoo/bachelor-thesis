package strategies.old.service;

import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import static strategies.old.Constants.DATA_PATH;

public class CompressionService {
    public void compressData(){
        compressFolder("characters");
        compressFolder("items");
        compressFolder("obstacles");
    }

    public void decompressData(){
        decompressFolder("characters");
        decompressFolder("items");
        decompressFolder("obstacles");
    }

    private void compressFolder(String path){
        try{
            new ZipFile(DATA_PATH + path + ".zip").addFolder(new File(DATA_PATH + path));
            deleteDirectoryRecursive(new File(DATA_PATH + path).toPath());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void decompressFolder(String path){
        try{
            new ZipFile(DATA_PATH + path + ".zip").extractAll(DATA_PATH);
            new File(DATA_PATH + path + ".zip").delete();
        }
        catch (Exception e){
            e.printStackTrace();
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
}
