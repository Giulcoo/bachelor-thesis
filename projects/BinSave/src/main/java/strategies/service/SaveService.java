package strategies.service;

import strategies.Constants;
import strategies.model.Change;
import strategies.model.Chunk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaveService {
    private FileOutputStream changeOutput;
    private FileInputStream changeInput;

    private List<Chunk.Builder> changedChunks = new ArrayList<>();
    private List<Chunk.Builder> removedChunks = new ArrayList<>();

    public void addChangedChunk(Chunk.Builder chunk){
        changedChunks.add(chunk);
    }

    public void addRemovedChunk(Chunk.Builder chunk){
        removedChunks.add(chunk);
    }

    public void saveChanges(){
        changedChunks.forEach(this::saveChunk);
        removedChunks.forEach(this::removeChunk);

        changedChunks.clear();
        removedChunks.clear();
    }

    private void saveChunk(Chunk.Builder chunk){
        //TODO: Check if chunk-file exists (add if not)
        //TODO: Overwrite chunk-file with new data
    }

    private void removeChunk(Chunk.Builder chunk){
        //TODO: Remove chunk-file
    }

    public void saveChange(Change.Builder change){
        try{
            if(this.changeOutput == null) this.changeOutput = new FileOutputStream(Constants.CHANGE_FILE);

            change.build().writeDelimitedTo(this.changeOutput);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<Change> getChanges(){
        ArrayList<Change> changes = new ArrayList<>();

        try{
            if(this.changeInput == null) this.changeInput = new FileInputStream(Constants.CHANGE_FILE);

            Change change = Change.parseDelimitedFrom(this.changeInput);
            while(change != null){
                changes.add(change);
                change = Change.parseDelimitedFrom(this.changeInput);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return changes;
    }

    public void close(){
        try{
            if(this.changeOutput != null) this.changeOutput.close();
            if(this.changeInput != null) this.changeInput.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
