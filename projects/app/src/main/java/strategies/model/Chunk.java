package strategies.model;

import java.util.ArrayList;
import java.util.List;

public class Chunk<T> {
    private final static int CHUNK_SIZE_LIMIT = 10;
    private final List<T> elements;

    private Vector center;
    private Vector size;

    public Chunk(Vector center, Vector size) {
        this.elements = new ArrayList<>();
    }

    public List<T> getElements() {
        return elements;
    }

    public List<Chunk<T>> checkChunkSize(){
        return elements.size() > CHUNK_SIZE_LIMIT ? splitChunk() : new ArrayList<>();
    }

    private List<Chunk<T>> splitChunk(){
        List<T> elementsCopy = new ArrayList<>(this.elements);
        this.elements.clear();

        Vector newSize = new Vector(size.getX()/2,size.getY()/2,0);

        List<Chunk<T>> result = new ArrayList<>();
        result.add(new Chunk<T>(new Vector(center.getX() - newSize.getX(),center.getY() - newSize.getY(),0), newSize));
        result.add(new Chunk<T>(new Vector(center.getX() + newSize.getX(),center.getY() - newSize.getY(),0), newSize));
        result.add(new Chunk<T>(new Vector(center.getX() - newSize.getX(),center.getY() + newSize.getY(),0), newSize));
        result.add(new Chunk<T>(new Vector(center.getX() + newSize.getX(),center.getY() + newSize.getY(),0), newSize));

        elementsCopy.forEach(e -> {
            for(Chunk c : result){
                if(c.pointInChunk(e)){
                    c.elements.add(e);
                    break;
                }
            }
        });

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
}
