package strategies.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import strategies.model.Character;
import strategies.model.Chunk;
import strategies.model.Item;
import strategies.model.Vector;

import java.util.ArrayList;
import java.util.List;

public class MongoService {
    private final String DB_URL = "mongodb://Giulio:bachelor2023@localhost:27017";
    private final MongoClient client;
    private final MongoDatabase db;
    private final MongoCollection<Chunk> characters;
    private final MongoCollection<Document> items;
    private final MongoCollection<Chunk> obstacles;

    public MongoService() {
        client = MongoClients.create(DB_URL);

        db = client.getDatabase("Game");
        characters = db.getCollection("Characters", Chunk.class);
        items = db.getCollection("Items", Document.class);
        obstacles = db.getCollection("Obstacles", Chunk.class);

        //items.insertOne(new Chunk<>('i', null, new Vector(0,0,0), new Vector(0,0,0)));

        items.insertOne(new Document()
                .append("id", "i-2")
                .append("center", new Document()
                        .append("x", 0)
                        .append("y", 0)
                        .append("z", 0))
                .append("size", new Document()
                        .append("x", 0)
                        .append("y", 0)
                        .append("z", 0))
                .append("elements", new ArrayList<>())
                .append("parentChunk", null)
                .append("childChunks", new ArrayList<>()));
    }

    public void save() {

    }
}
