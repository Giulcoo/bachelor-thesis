package strategies.service;

import com.ctc.wstx.shaded.msv_core.verifier.jarv.Const;
import strategies.Constants;
import strategies.proto.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ProtoService {
    private Game.Builder game;
    private FileOutputStream output;
    private FileInputStream input;

    public void createGame(int dataCount){
        this.game = Game.newBuilder();

        Chunk.Builder chunk = Chunk.newBuilder();

        for(int i = 0; i < dataCount; i++){
            chunk.addPlayers(generatePlayer(i > 0));
        }

        game.addChunks(chunk);
    }

    public void randomChanges(int count){

    }

    public Player.Builder generatePlayer(boolean isBot){
        return Player.newBuilder()
            .setId(UUID.randomUUID().toString())
            .setName(isBot ? "Player" : "Monster")
            .setIsBot(isBot)
            .setPosition(Vector.newBuilder().setX(0).setY(0).build());
    }

    public void saveGame(){
        if(this.output == null){
            try{
                this.output = new FileOutputStream(Constants.DATA_PATH + "test");
            }
            catch(FileNotFoundException e){
                System.out.println("FileOutputStream: File not found");
            }
        }

        try{
            this.game.build().writeTo(this.output);
        }
        catch(IOException e){
            System.out.println("Saving not possible");
        }
    }

    public void loadGame(){
        if(this.input == null){
            try{
                this.input = new FileInputStream(Constants.DATA_PATH + "test");
            }
            catch(FileNotFoundException e){
                System.out.println("FileOutputStream: File not found");
            }
        }

        try{
            this.game = Game.parseFrom(this.input).toBuilder();
            System.out.println(this.game.getChunks(0).getPlayers(0).getName());
        }
        catch(IOException e){
            System.out.println("Loading not possible");
        }
    }

    public void close(){
        try{
            if(this.output != null) this.output.close();

            if(this.input != null) this.input.close();
        }
        catch(IOException e){
            System.out.println("Closing FileStreams not possible");
        }
    }
}
