package strategies.model;

import java.util.Collection;
import java.util.List;

public class GameInfo {
    private String rootChunk;
    private String playerChunk;
    private List<ChunkInfo> chunks;

    public String getRootChunk() {
        return rootChunk;
    }

    public GameInfo setRootChunk(String rootChunk) {
        this.rootChunk = rootChunk;
        return this;
    }

    public String getPlayerChunk() {
        return playerChunk;
    }

    public GameInfo setPlayerChunk(String playerChunk) {
        this.playerChunk = playerChunk;
        return this;
    }

    public List<ChunkInfo> getChunksList() {
        return chunks;
    }

    public int getChunksCount(){
        return this.chunks.size();
    }

    public GameInfo addChunk(ChunkInfo chunk){
        this.chunks.add(chunk);
        return this;
    }

    public GameInfo addChunk(int index, ChunkInfo chunk){
        this.chunks.add(index, chunk);
        return this;
    }

    public GameInfo removeChunk(int index){
        this.chunks.remove(index);
        return this;
    }

    public int indexOfChunk(ChunkInfo chunkInfo){
        int index = 0;
        for (ChunkInfo chunk : this.chunks) {
            if(chunk.getId().equals(chunkInfo.getId())){
                return index;
            }
            index++;
        }

        return -1;
    }

    public GameInfo addAllChunks(Collection<ChunkInfo> chunks){
        this.chunks.addAll(chunks);
        return this;
    }

    public GameInfo clearChunks(){
        this.chunks.clear();
        return this;
    }

    public GameInfo setChunksList(List<ChunkInfo> chunks) {
        this.chunks = chunks;
        return this;
    }
}
