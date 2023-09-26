package strategies.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Chunk {
    private String id;
    private List<Player> players;
    private Vector position;
    private Vector size;
    private String parentChunk;
    private List<String> childChunks;

    public String getId() {
        return id;
    }

    public Chunk setId(String id) {
        this.id = id;
        return this;
    }

    public List<Player> getPlayersList() {
        return players;
    }

    public Player getPlayer(String playerID){
        for (Player p : this.players) {
            if(p.getId().equals(playerID)){
                return p;
            }
        }
        return null;
    }

    public Chunk addPlayer(Player player) {
        this.players.add(player);
        return this;
    }

    public Chunk addPlayer(int index, Player player) {
        this.players.add(index, player);
        return this;
    }

    public Chunk removePlayer(int index){
        this.players.remove(index);
        return this;
    }

    public int indexOfPlayer(Player player){
        int index = 0;
        for (Player p : this.players) {
            if(p.getId().equals(player.getId())){
                return index;
            }
            index++;
        }
        return -1;
    }

    public Chunk addAllPlayers(Collection<Player> players) {
        this.players.addAll(players);
        return this;
    }

    public int getPlayersCount(){
        return players.size();
    }

    public Chunk clearPlayers(){
        this.players.clear();
        return this;
    }

    public Chunk setPlayersList(List<Player> players) {
        this.players = players;
        return this;
    }

    public Vector getPosition() {
        return position;
    }

    public Chunk setPosition(Vector position) {
        this.position = position;
        return this;
    }

    public Vector getSize() {
        return size;
    }

    public Chunk setSize(Vector size) {
        this.size = size;
        return this;
    }

    public String getParentChunk() {
        return parentChunk;
    }

    public Chunk setParentChunk(String parentChunk) {
        this.parentChunk = parentChunk;
        return this;
    }

    public List<String> getChildChunksList() {
        return childChunks;
    }

    public int getChildChunksCount(){
        return childChunks.size();
    }

    public Chunk addChildChunk(String childChunk){
        this.childChunks.add(childChunk);
        return this;
    }

    public Chunk addChildChunk(int index, String childChunk){
        this.childChunks.add(index, childChunk);
        return this;
    }

    public Chunk removeChildChunk(int index){
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

    public Chunk addAllChildChunks(Collection<String> childChunks){
        this.childChunks.addAll(childChunks);
        return this;
    }

    public Chunk clearChildChunks(){
        this.childChunks.clear();
        return this;
    }

    public Chunk setChildChunksList(List<String> childChunks) {
        this.childChunks = childChunks;
        return this;
    }

    public ChunkInfo toInfo(){
        return new ChunkInfo()
                .setId(this.id)
                .setSize(new Vector(this.size))
                .setPosition(new Vector(this.position))
                .setParentChunk(this.parentChunk)
                .setChildChunksList(new ArrayList<>(this.childChunks));
    }
}
