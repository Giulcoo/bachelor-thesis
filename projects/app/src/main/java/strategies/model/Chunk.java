package strategies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class Chunk<T> {

    @JsonIgnore private final static int CHUNK_SIZE_LIMIT = 5;
    @JsonIgnore private final static int CHUNK_GROUP_SIZE_MIN = 2;

    private final String id;
    private final Vector center;
    private final Vector size;
    @JsonIgnore final private List<T> elements;

    @JsonIgnore private Chunk<T> parentChunk;
    @JsonIgnore private List<Chunk<T>> childChunks;

    public Chunk(java.lang.Character type, Chunk<T> parentChunk, Vector center, Vector size) {
        this.id = type + "-" + UUID.randomUUID().toString();
        this.center = center;
        this.size = size;
        this.elements = new ArrayList<>();
        this.parentChunk = parentChunk;
        this.childChunks = new ArrayList<>();
    }

    public Chunk(String id, Vector center, Vector size){
        this.id = id;
        this.center = center;
        this.size = size;
        this.elements = new ArrayList<>();
        this.childChunks = new ArrayList<>();
        this.parentChunk = null;
    }

    public String getId() {
        return id;
    }

    @JsonIgnore
    public String getElementId(){
        return id.charAt(0) + "e" + id.substring(1);
    }

    @JsonIgnore
    public Chunk<T> getParent(){
        return parentChunk;
    }

    @JsonIgnore
    public List<Chunk<T>> getChildChunks(){
        return childChunks;
    }

    public List<String> getChildChunksIds(){
        return childChunks.stream().map(Chunk::getId).toList();
    }

    @JsonIgnore
    public List<T> getElements() {
        return elements;
    }

    public Vector getCenter() {
        return center;
    }

    public Vector getSize() {
        return size;
    }

    public Chunk<T> setParent(Chunk<T> parentChunk){
        this.parentChunk = parentChunk;
        return this;
    }

    public Chunk addElement(T element){
        if(element instanceof Item) ((Item) element).setChunk((Chunk<Item>) this);
        else if(element instanceof Character) ((Character) element).setChunk((Chunk<Character>) this);
        elements.add(element);
        return this;
    }

    public Chunk addElements(List<T> elements){
        elements.forEach(this::addElement);
        return this;
    }

    public Chunk removeElement(T element){
        elements.remove(element);
        if(element instanceof Item) ((Item) element).setChunk(null);
        else if(element instanceof Character) ((Character) element).setChunk(null);
        return this;
    }

    public Chunk addChild(Chunk<T> childChunk){
        childChunks.add(childChunk);
        return this;
    }

    public Chunk addChildren(List<Chunk<T>> childChunks){
        childChunks.forEach(this::addChild);
        return this;
    }

    public Chunk clearChildren(){
        childChunks.clear();
        //childrenIds.clear();
        return this;
    }

    /**
     Checks if a chunk has to be split or joined
     @return -1 if the chunk was joined<br>1 &nbsp;if the chunk was splitted<br>0 &nbsp;if nothing happened
     */
    public int checkChunkSize(){
        List<Chunk<T>> childsToCheck = parentChunk != null ? parentChunk.getChildChunks() : childChunks;
        if(!childsToCheck.isEmpty() && childsToCheck.stream().mapToInt(c -> c.elements.size()).sum() < CHUNK_GROUP_SIZE_MIN){
            joinChunk();
            return -1;
        }

        if(elements.size() > CHUNK_SIZE_LIMIT){
            splitChunk();
            return 1;
        }

        return 0;
    }

    private void splitChunk(){
        List<T> elementsCopy = new ArrayList<>(this.elements);
        this.elements.clear();

        Vector newSize = new Vector(size.getX()/2,size.getY()/2,0);

        List<Chunk<T>> result = new ArrayList<>();
        result.add(new Chunk<T>(id.charAt(0), this, new Vector(center.getX() - newSize.getX()/2,center.getY() - newSize.getY()/2,0), newSize));
        result.add(new Chunk<T>(id.charAt(0), this, new Vector(center.getX() + newSize.getX()/2,center.getY() - newSize.getY()/2,0), newSize));
        result.add(new Chunk<T>(id.charAt(0), this, new Vector(center.getX() - newSize.getX()/2,center.getY() + newSize.getY()/2,0), newSize));
        result.add(new Chunk<T>(id.charAt(0), this, new Vector(center.getX() + newSize.getX()/2,center.getY() + newSize.getY()/2,0), newSize));
        this.addChildren(result);

        //Split chunk elements in new chunks
        elementsCopy.forEach(e -> {
            for(Chunk c : result){
                if(c.pointInChunk(e)){
                    c.addElement(e);
                    break;
                }
            }
        });
    }

    private void joinChunk(){
        this.parentChunk.getChildChunks().forEach(c -> {
            this.parentChunk.addElements(c.elements);
            c.elements.clear();
        });
    }

    public Chunk findChunk(Object o){
        if(pointInChunk(o)){
            if(childChunks.isEmpty()) return this;
            else{
                for(Chunk c : childChunks){
                    if(c.pointInChunk(o)){
                        Chunk result = c.findChunk(o);
                        if(result != null) return result;
                    }
                }
            }
        }

        return this.parentChunk.findChunk(o);
    }

    public List<Chunk<T>> chunksInArea(Vector position, double size){
        if(rectInChunk(position, size)){
            if(childChunks.isEmpty()) return List.of(this);
            else{
                List<Chunk<T>> result = new ArrayList<>();
                for(Chunk<T> c : childChunks){
                    if(c.rectInChunk(position, size)) result.addAll(c.chunksInArea(position, size));
                }
                return result;
            }
        }

        return new ArrayList<>();
    }

    public boolean pointInChunk(Object o){
        Vector position = null;
        if (o instanceof Character) {
            position = ((Character) o).getPosition();
        } else if (o instanceof Item) {
            position = ((Item) o).getPosition();
        } else if (o instanceof Obstacle) {
            position = ((Obstacle) o).getPosition();
        }

        return position != null && pointInChunk(position);
    }

    public boolean pointInChunk(Vector position){
        return Math.abs(position.getX() - center.getX()) <= size.getX() / 2
                && Math.abs(position.getY() - center.getY()) <= size.getY() / 2;
    }

    public boolean rectInChunk(Vector center, double size){
        Vector leftTop = new Vector(center.getX() - size/2, center.getY() - size/2, 0);
        Vector bottomRight = new Vector(center.getX() + size/2, center.getY() + size/2, 0);

        return leftTop.getX() <= this.center.getX() + this.size.getX()/2
                && leftTop.getY() <= this.center.getY() + this.size.getY()/2
                && bottomRight.getX() >= this.center.getX() - this.size.getX()/2
                && bottomRight.getY() >= this.center.getY() - this.size.getY()/2;
    }

    @Override
    public String toString() {
        List<String> childrenIDs = this.getChildChunksIds();
        String result = "  ID: " + id + " | Pos: " + center.toString2D() + " | Size: " + size.getX();

        if(!childrenIDs.isEmpty()){
            result += "\n  Children (" + childrenIDs.size() + "): [";
            for(String id : childrenIDs){
                result += id + " ";
            }
            result += "]";
        }

        if(!elements.isEmpty()){
            result += "]\n  Elemenst (" + elements.size() + "):\n";
            for(T element : elements){
                result += "    " + element.toString() + "\n";
            }
        }
        return result;
    }
}
