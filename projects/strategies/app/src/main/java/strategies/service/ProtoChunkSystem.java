package strategies.service;

import strategies.proto.Chunk;
import strategies.proto.Game;
import strategies.proto.Player;

public class ProtoChunkSystem {
    private final Game.Builder game;
    private final boolean dynamicChunkSize;

    public ProtoChunkSystem(Game.Builder game, boolean dynamicChunkSize) {
        this.game = game;
        this.dynamicChunkSize = dynamicChunkSize;
    }

    public void createChunks(){
        if(dynamicChunkSize){
            Chunk.Builder chunk = Chunk.newBuilder();
            this.game.addChunks(chunk);
        }
        else{

        }
    }

    public void addPlayer(Player.Builder player){

    }

    public void removePlayer(){

    }

    public void updatePlayer(){

    }
}
