package strategies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChunkInfo {
    private String id;
    private Vector position;
    private Vector size;
    private String parentChunk;
    private List<String> childChunks = new ArrayList<>();

    public String getId() {
        return id;
    }

    public ChunkInfo setId(String id) {
        this.id = id;
        return this;
    }

    public Vector getPosition() {
        return position;
    }

    public ChunkInfo setPosition(Vector position) {
        this.position = position;
        return this;
    }

    public Vector getSize() {
        return size;
    }

    public ChunkInfo setSize(Vector size) {
        this.size = size;
        return this;
    }

    public String getParentChunk() {
        return parentChunk;
    }

    public ChunkInfo setParentChunk(String parentChunk) {
        this.parentChunk = parentChunk;
        return this;
    }

    public List<String> getChildChunksList() {
        return childChunks;
    }

    @JsonIgnore
    public int getChildChunksCount(){
        return childChunks.size();
    }

    public ChunkInfo addChildChunk(String childChunk){
        this.childChunks.add(childChunk);
        return this;
    }

    public ChunkInfo addChildChunk(int index, String childChunk){
        this.childChunks.add(index, childChunk);
        return this;
    }

    public ChunkInfo removeChildChunk(int index){
        this.childChunks.remove(index);
        return this;
    }

    public int indexOfChildChunk(String childChunk){
        int index = 0;
        for (String chunk : this.childChunks) {
            if(chunk.equals(childChunk)){
                return index;
            }
            index++;
        }

        return -1;
    }

    public ChunkInfo addAllChildChunks(Collection<String> childChunks){
        this.childChunks.addAll(childChunks);
        return this;
    }

    public ChunkInfo clearChildChunks(){
        this.childChunks.clear();
        return this;
    }

    public ChunkInfo setChildChunksList(List<String> childChunks) {
        this.childChunks = childChunks;
        return this;
    }

    public Chunk toChunk(){
        return new Chunk()
                .setId(this.id)
                .setPosition(new Vector(this.position))
                .setSize(new Vector(this.size))
                .setParentChunk(this.parentChunk)
                .setChildChunksList(new ArrayList<>(this.childChunks));
    }
}
