package strategies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class Chunk<T> {

    @JsonIgnore private final static int CHUNK_SIZE_LIMIT = 20;
    @JsonIgnore  private final static int CHUNK_GROUP_SIZE_MIN = 5;
    private final String id;
    private Vector center;
    private Vector size;
    private final List<T> elements;
    private final List<String> childrenIds;
    @JsonIgnore private final Chunk<T> parentChunk;
    @JsonIgnore private final List<Chunk<T>> childChunks;

    /*private final List<Chunk<T>> topChunks;

    private final List<Chunk<T>> leftChunks;

    private final List<Chunk<T>> rightChunks;

    private final List<Chunk<T>> bottomChunks;*/


    public Chunk(Chunk<T> parentChunk, Vector center, Vector size) {
        this.id = UUID.randomUUID().toString();
        this.center = center;
        this.size = size;
        this.elements = new ArrayList<>();
        this.parentChunk = parentChunk;
        this.childChunks = new ArrayList<>();
        this.childrenIds = new ArrayList<>();
        /*this.topChunks = new ArrayList<>();
        this.leftChunks = new ArrayList<>();
        this.rightChunks = new ArrayList<>();
        this.bottomChunks = new ArrayList<>();*/
    }

    public String getId() {
        return id;
    }

    public Chunk<T> getParent(){
        return parentChunk;
    }

    public List<Chunk<T>> getChildChunks(){
        return childChunks;
    }

    public List<T> getElements() {
        return elements;
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
        childrenIds.add(childChunk.getId());
        return this;
    }

    public Chunk addChildren(List<Chunk<T>> childChunks){
        childChunks.forEach(this::addChild);
        return this;
    }

    public Chunk clearChildren(){
        childChunks.clear();
        childrenIds.clear();
        return this;
    }

    /*public Chunk setNeighbours(List<Chunk<T>> topChunks, List<Chunk<T>> leftChunks, List<Chunk<T>> rightChunks, List<Chunk<T>> bottomChunks){
        this.topChunks.addAll(topChunks);
        this.leftChunks.addAll(leftChunks);
        this.rightChunks.addAll(rightChunks);
        this.bottomChunks.addAll(bottomChunks);
        return this;
    }

    public List<Chunk<T>> getNeighbours(){
        List<Chunk<T>> result = new ArrayList<>();
        result.addAll(topChunks);
        result.addAll(leftChunks);
        result.addAll(rightChunks);
        result.addAll(bottomChunks);
        return result;
    }*/

    /**
     Checks if a chunk has to be split or joined
     @return -1 if the chunk was joined<br>1 &nbsp;if the chunk was splitted<br>0 &nbsp;if nothing happened
     */
    public int checkChunkSize(){
        /*System.out.println("\n-----------------------------------------------------\nChecking chunk " + center + " elements: " + elements.size());*/
        List<Chunk<T>> childsToCheck = parentChunk != null ? parentChunk.getChildChunks() : childChunks;
        if(!childsToCheck.isEmpty() && childsToCheck.stream().mapToInt(c -> c.elements.size()).sum() < CHUNK_GROUP_SIZE_MIN){
            joinChunk();
            /*System.out.println("Joining chunk elements");*/
            return -1;
        }

        if(elements.size() > CHUNK_SIZE_LIMIT){
            splitChunk();
            /*String childs = "";
            for(Chunk<T> c : childChunks) childs += c.center + " ";
            System.out.println("Splitting chunk elements: " + childs);*/
            return 1;
        }

        return 0;

        //return elements.size() > CHUNK_SIZE_LIMIT ? splitChunk() : 0;
    }

    private void splitChunk(){
        List<T> elementsCopy = new ArrayList<>(this.elements);
        this.elements.clear();

        Vector newSize = new Vector(size.getX()/2,size.getY()/2,0);

        List<Chunk<T>> result = new ArrayList<>();
        result.add(new Chunk<T>(this, new Vector(center.getX() - newSize.getX()/2,center.getY() - newSize.getY()/2,0), newSize));
        result.add(new Chunk<T>(this, new Vector(center.getX() + newSize.getX()/2,center.getY() - newSize.getY()/2,0), newSize));
        result.add(new Chunk<T>(this, new Vector(center.getX() - newSize.getX()/2,center.getY() + newSize.getY()/2,0), newSize));
        result.add(new Chunk<T>(this, new Vector(center.getX() + newSize.getX()/2,center.getY() + newSize.getY()/2,0), newSize));
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

       /* result.get(0).setNeighbours(new ArrayList<>(topChunks), new ArrayList<>(leftChunks), Arrays.asList(result.get(1)), Arrays.asList(result.get(2)));
        result.get(1).setNeighbours(new ArrayList<>(topChunks), Arrays.asList(result.get(0)), new ArrayList<>(rightChunks), Arrays.asList(result.get(3)));
        result.get(2).setNeighbours(Arrays.asList(result.get(0)), new ArrayList<>(leftChunks), Arrays.asList(result.get(3)), new ArrayList<>(bottomChunks));
        this.setNeighbours(Arrays.asList(result.get(1)), Arrays.asList(result.get(2)), new ArrayList<>(rightChunks), new ArrayList<>(bottomChunks));

        this.elements.addAll(result.get(3).elements);
        this.center = result.get(3).center;
        this.size = result.get(3).size;
        result.remove(3);
        return result;*/
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

    public boolean pointInChunk(Object o){
        Vector position = null;
        if (o instanceof Character) {
            position = ((Character) o).getPosition();
        } else if (o instanceof Item) {
            position = ((Item) o).getPosition();
        } else if (o instanceof Obstacle) {
            position = ((Obstacle) o).getPosition();
        }

        return position != null
                && Math.abs(position.getX() - center.getX()) <= size.getX() / 2
                && Math.abs(position.getY() - center.getY()) <= size.getY() / 2;
    }

    @Override
    public String toString() {
        String result = "  ID: " + id + "\n  Position: " + center + " Size: " + size + "\n  Children (" + childrenIds.size() + "): [";
        for(String id : childrenIds){
            result += id + " ";
        }

        result += "]\n  Elemenst (" + elements.size() + "):\n";
        for(T element : elements){
            result += "    " + element.toString() + "\n";
        }
        return result;
    }
}
