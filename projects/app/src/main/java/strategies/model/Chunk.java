package strategies.model;

import java.sql.SQLOutput;
import java.util.*;

public class Chunk<T> {
    private final List<T> elements;
    private final String id;
    private final static int CHUNK_SIZE_LIMIT = 100;

    private final List<Chunk<T>> topChunks;

    private final List<Chunk<T>> leftChunks;

    private final List<Chunk<T>> rightChunks;

    private final List<Chunk<T>> bottomChunks;

    private Vector center;
    private Vector size;

    public Chunk(Vector center, Vector size) {
        this.id = UUID.randomUUID().toString();
        this.center = center;
        this.size = size;
        this.elements = new ArrayList<>();
        this.topChunks = new ArrayList<>();
        this.leftChunks = new ArrayList<>();
        this.rightChunks = new ArrayList<>();
        this.bottomChunks = new ArrayList<>();
    }

    public String getId() {
        return id;
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

    public Chunk setNeighbours(List<Chunk<T>> topChunks, List<Chunk<T>> leftChunks, List<Chunk<T>> rightChunks, List<Chunk<T>> bottomChunks){
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
    }

    public List<Chunk<T>> checkChunkSize(){
        return elements.size() > CHUNK_SIZE_LIMIT ? splitChunk() : new ArrayList<>();
    }

    private List<Chunk<T>> splitChunk(){
        List<T> elementsCopy = new ArrayList<>(this.elements);
        this.elements.clear();

        Vector newSize = new Vector(size.getX()/2,size.getY()/2,0);

        List<Chunk<T>> result = new ArrayList<>();
        result.add(new Chunk<T>(new Vector(center.getX() - newSize.getX()/2,center.getY() - newSize.getY()/2,0), newSize));
        result.add(new Chunk<T>(new Vector(center.getX() + newSize.getX()/2,center.getY() - newSize.getY()/2,0), newSize));
        result.add(new Chunk<T>(new Vector(center.getX() - newSize.getX()/2,center.getY() + newSize.getY()/2,0), newSize));
        result.add(new Chunk<T>(new Vector(center.getX() + newSize.getX()/2,center.getY() + newSize.getY()/2,0), newSize));

        elementsCopy.forEach(e -> {
            for(Chunk c : result){
                if(c.pointInChunk(e)){
                    c.addElement(e);
                    break;
                }
            }
        });

        result.get(0).setNeighbours(new ArrayList<>(topChunks), new ArrayList<>(leftChunks), Arrays.asList(result.get(1)), Arrays.asList(result.get(2)));
        result.get(1).setNeighbours(new ArrayList<>(topChunks), Arrays.asList(result.get(0)), new ArrayList<>(rightChunks), Arrays.asList(result.get(3)));
        result.get(2).setNeighbours(Arrays.asList(result.get(0)), new ArrayList<>(leftChunks), Arrays.asList(result.get(3)), new ArrayList<>(bottomChunks));
        this.setNeighbours(Arrays.asList(result.get(1)), Arrays.asList(result.get(2)), new ArrayList<>(rightChunks), new ArrayList<>(bottomChunks));

        this.elements.addAll(result.get(3).elements);
        this.center = result.get(3).center;
        this.size = result.get(3).size;

        result.remove(3);
        return result;
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
        String result = "Position: " + center + " Size: " + size + "\nElemenst:\n";
        for(T element : elements){
            result += element.toString() + "\n";
        }
        return result;
    }
}
