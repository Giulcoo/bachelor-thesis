package benchmarks;

import org.openjdk.jmh.annotations.*;
import strategies.App;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class Benchmarks {
    @Param({"1000", "10000", "100000"})
    public static int dataCount;

    @State(Scope.Benchmark)
    public static class CreateData {
        @Setup(Level.Iteration)
        public void setUp() {
            App.createGame(dataCount);
        }
    }

    @Benchmark
    public static void createGame(){
        App.createGame(dataCount);
    }

    @Benchmark
    public static void loadGame(CreateData createData){
        App.loadGame();
    }

//    @Benchmark
//    public static void createGameAndPlay(){
//        App.createGameAndPlay(dataCount, 100, 100,100,50);
//    }
//
//    @Benchmark
//    public static void createWithGrowingAmountOfBots(){
//        App.createGameAndPlay(dataCount, 100, 100,1000,10);
//    }
//
//    @Benchmark
//    public static void loadGameAndPlay(CreateData createData){
//        App.loadGameAndPlay(100, 100, 100,50);
//    }
//
//    @Benchmark
//    public static void loadWithGrowingAmountOfBots(CreateData createData){
//        App.loadGameAndPlay(100, 100,1000,10);
//    }

}