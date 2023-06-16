package strategies.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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
    private final MongoCollection<Chunk> items;
    private final MongoCollection<Chunk> obstacles;

    public MongoService() {
        client = MongoClients.create(DB_URL);

        db = client.getDatabase("Game");
        characters = db.getCollection("characters", Chunk.class);
        items = db.getCollection("items", Chunk.class);
        obstacles = db.getCollection("obstacles", Chunk.class);

        List<Chunk<Item>> items = new ArrayList<>();

        items.add(new  Chunk('c', null, new Vector(0,0,0), new Vector(0,0,0)));
        characters.insertMany(items);
    }

    public void save() {

    }
}
