package strategies.service;

import com.google.protobuf.Any;
import strategies.Constants;
import strategies.model.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChangeFileService {
    private FileOutputStream changeOutput;
    private FileInputStream changeInput;

    /** Change File: Add new Chunk */
    public void saveChunkAdded(Chunk.Builder chunk){
        saveChunkChange(chunk.getId(), Change.Event.ADDED, "", Any.pack(chunk.build()));
    }

    /** Change File: Update childChunks of chunk */
    public void saveChunkUpdate(String chunkID, List<String> childChunks){
        saveChunkChange(chunkID, Change.Event.UPDATED, "childChunks",
                Any.pack(StringArrayWrapper.newBuilder().addAllValue(childChunks).build()));
    }

    /** Change File: Remove chunk */
    public void saveChunkRemoved(String chunkID){
        saveChunkChange(chunkID, Change.Event.REMOVED, "", null);
    }

    /** Change File: General chunk changes */
    public void saveChunkChange(String chunkID, Change.Event event, String key, Any any){
        saveChange(Change.newBuilder()
                .setId(chunkID)
                .setType(Change.Type.CHUNK)
                .setEvent(event)
                .setKey(key)
                .setValue(any)
        );
    }


    /** Change File: Add new player */
    public void savePlayerAdded(Player.Builder player){
        savePlayerChange(player.getId(), Change.Event.ADDED, "", Any.pack(player.build()));
    }

    /** Change File: Update chunk of player */
    public void savePlayerUpdate(String playerID, String chunk){
        savePlayerChange(playerID, Change.Event.UPDATED, "chunk",
                Any.pack(StringWrapper.newBuilder().setValue(chunk).build()));
    }

    /** Change File: Remove player */
    public void savePlayerRemove(String playerID, String chunk){
        savePlayerChange(playerID, Change.Event.REMOVED, "chunk",
                Any.pack(StringWrapper.newBuilder().setValue(chunk).build()));
    }

    /** Change File: Update position of player */
    public void savePlayerUpdate(String playerID, Vector position){
        savePlayerChange(playerID, Change.Event.UPDATED, "position",
                Any.pack(position));
    }

    /** Change File: General player changes */
    public void savePlayerChange(String playerID, Change.Event event, String key, Any any){
        saveChange(Change.newBuilder()
                .setId(playerID)
                .setType(Change.Type.PLAYER)
                .setEvent(event)
                .setKey(key)
                .setValue(any)
        );
    }

    private void saveChange(Change.Builder change){
        try{
            if(this.changeOutput == null) this.changeOutput = new FileOutputStream(Constants.CHANGE_FILE);

            change.build().writeDelimitedTo(this.changeOutput);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /** Change File: Get saved changes */
    public List<Change> getChanges(){
        ArrayList<Change> changes = new ArrayList<>();

        try{
            if(this.changeInput == null) this.changeInput = new FileInputStream(Constants.CHANGE_FILE);

            Change change = Change.parseDelimitedFrom(this.changeInput);
            while(change != null){
                changes.add(change);
                change = Change.parseDelimitedFrom(this.changeInput);
            }

            //TODO: Clear change file
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return changes;
    }

    /** Close all active FileStreams */
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
