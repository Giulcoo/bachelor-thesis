package strategies.service;

import strategies.model.Chunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkFileService {
    private final List<String> changedChunkIDs = new ArrayList<>();
    private final List<Chunk.Builder> changedChunks = new ArrayList<>();
    private final List<String> removedChunkIDs = new ArrayList<>();

    /** Chunk Saving: Add changed chunk */
    public void addChangedChunk(Chunk.Builder chunk){
        if(!changedChunkIDs.contains(chunk.getId())){
            changedChunkIDs.add(chunk.getId());
            changedChunks.add(chunk);
        }
    }

    public void addChangedChunks(List<Chunk.Builder> chunks){
        chunks.forEach(this::addChangedChunk);
    }

    public void addRemovedChunk(String chunkID){
        if(!removedChunkIDs.contains(chunkID)){
            removedChunkIDs.add(chunkID);
        }
    }

    /** Chunk Saving: Add removed chunk */
    public void addRemovedChunk(Chunk.Builder chunk){
        addRemovedChunk(chunk.getId());
    }

    /** Chunk Saving: Add multiple removed chunks */
    public void addRemovedChunks(List<Chunk.Builder> chunks){
        chunks.forEach(this::addRemovedChunk);
    }



    /** Chunk Saving: Save every changed chunk and delete files of removed chunks */
    public void saveChanges(){
        changedChunks.forEach(this::saveChunk);
        removedChunkIDs.forEach(this::removeChunk);

        changedChunks.clear();
        changedChunkIDs.clear();
        removedChunkIDs.clear();
    }

    private void saveChunk(Chunk.Builder chunk){
        //TODO: Check if chunk-file exists (add if not)
        //TODO: Overwrite chunk-file with new data
    }

    private void removeChunk(String chunkID){
        //TODO: Remove chunk-file
    }

    public Chunk getChunk(String chunkID){
        //TODO: Return chunk
        return null;
    }

    /** Close all active FileStreams */
    public void close(){

    }
}
