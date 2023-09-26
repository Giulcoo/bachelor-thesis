package strategies.service;

import strategies.model.Change;
import strategies.model.Chunk;
import strategies.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ChangeFileService {
    /** Change File: Add new Chunk */
    public void saveChunkAdded(Chunk chunk){
        //TODO
    }

    /** Change File: Update childChunks of chunk */
    public void saveChunkUpdate(String chunkID, List<String> childChunks){
        //TODO
    }

    /** Change File: Remove chunk */
    public void saveChunkRemoved(String chunkID){
        //TODO
    }

    /** Change File: General chunk changes */
//    public void saveChunkChange(String chunkID, Change.Event event, String key, Any any){
////        //TODO
////    }


    /** Change File: Add new player */
    public void savePlayerAdded(Player player){
        //TODO
    }

    /** Change File: Update chunk of player */
    public void savePlayerUpdate(String playerID, String oldChunkID, String newChunkID){
        //TODO
    }

    /** Change File: Update position of player */
    public void savePlayerUpdate(Player player){
        //TODO
    }

    /** Change File: Remove player */
    public void savePlayerRemove(String playerID, String chunkID){
        //TODO
    }

    /** Change File: General player changes */
//    public void savePlayerChange(String chunkID, String playerID, Change.Event event, String key, Any any){
//        //TODO
//    }

    private void saveChange(Change change){
        //TODO
        // Maybe Type Problem
        // Maybe not working with change but JsonObject would be better
    }

    /** Change File: Get saved changes */
    public List<Change> getChanges(){
        ArrayList<Change> changes = new ArrayList<>();

       //TODO

        return changes;
    }

    /** Close all active FileStreams */
    public void close(){
        //TODO maybe not needed
    }
}
