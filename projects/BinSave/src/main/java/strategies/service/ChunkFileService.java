package strategies.service;

import strategies.model.Chunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkFileService {
    private List<Chunk.Builder> changedChunks = new ArrayList<>();
    private List<Chunk.Builder> removedChunks = new ArrayList<>();

    /** Chunk Saving: Add changed chunk */
    public void addChangedChunk(Chunk.Builder chunk){
        changedChunks.add(chunk);
    }

    /** Chunk Saving: Add removed chunk */
    public void addRemovedChunk(Chunk.Builder chunk){
        removedChunks.add(chunk);
    }

    /** Chunk Saving: Add multiple removed chunks */
    public void addRemovedChunks(List<Chunk.Builder> chunks){
        chunks.forEach(this::addRemovedChunk);
    }

    /** Chunk Saving: Save every changed chunk and delete files of removed chunks */
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

    /** Close all active FileStreams */
    public void close(){

    }
}
