package benchmarks;

import org.openjdk.jmh.annotations.*;
import strategies.App;
import strategies.Constants;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class StaticBenchmark {
    @Param({"true", "false"})
    public static boolean useChangeFile;
    @Param({"true", "false"})
    public static boolean useGzip;
    @Param({"1000", "10000", "100000"})
    public static int dataCount;
    @Param({"2", "10", "50"})
    public static int staticChunkAmount;

    @State(Scope.Benchmark)
    public static class CreateData {
        @Setup(Level.Iteration)
        public void setUp() {
            Constants.DYNAMIC_CHUNK_SIZE = false;
            Constants.USE_CHANGE_FILE = useChangeFile;
            Constants.USE_GZIP = useGzip;
            Constants.STATIC_CHUNK_AMOUNT = staticChunkAmount;
            Constants.STATIC_CHUNK_SIZE = Constants.MAP_SIZE/staticChunkAmount;

            App.createGame(dataCount);
        }
    }

    @Benchmark
    public static void createGame(){
        Constants.DYNAMIC_CHUNK_SIZE = false;
        Constants.USE_CHANGE_FILE = useChangeFile;
        Constants.USE_GZIP = useGzip;
        Constants.STATIC_CHUNK_AMOUNT = staticChunkAmount;
        Constants.STATIC_CHUNK_SIZE = Constants.MAP_SIZE/staticChunkAmount;

        App.createGame(dataCount);
    }

    @Benchmark
    public static void loadGame(CreateData createData){
        Constants.DYNAMIC_CHUNK_SIZE = false;
        Constants.USE_CHANGE_FILE = useChangeFile;
        Constants.USE_GZIP = useGzip;
        Constants.STATIC_CHUNK_AMOUNT = staticChunkAmount;
        Constants.STATIC_CHUNK_SIZE = Constants.MAP_SIZE/staticChunkAmount;

        App.loadGame();
    }

//    @Benchmark
//    public static void createPlayers(CreateData createData){
//        Constants.DYNAMIC_CHUNK_SIZE = false;
//        Constants.USE_CHANGE_FILE = useChangeFile;
//        Constants.USE_GZIP = useGzip;
//        Constants.STATIC_CHUNK_AMOUNT = staticChunkAmount;
//        Constants.STATIC_CHUNK_SIZE = Constants.MAP_SIZE/staticChunkAmount;
//
//        App.createPlayers(dataCount);
//    }
//
//    @Benchmark
//    public static void removePlayers(CreateData createData){
//        Constants.DYNAMIC_CHUNK_SIZE = false;
//        Constants.USE_CHANGE_FILE = useChangeFile;
//        Constants.USE_GZIP = useGzip;
//        Constants.STATIC_CHUNK_AMOUNT = staticChunkAmount;
//        Constants.STATIC_CHUNK_SIZE = Constants.MAP_SIZE/staticChunkAmount;
//
//        App.removePlayers(dataCount-10);
//    }
//
//    @Benchmark
//    public static void movePlayers(CreateData createData){
//        Constants.DYNAMIC_CHUNK_SIZE = false;
//        Constants.USE_CHANGE_FILE = useChangeFile;
//        Constants.USE_GZIP = useGzip;
//        Constants.STATIC_CHUNK_AMOUNT = staticChunkAmount;
//        Constants.STATIC_CHUNK_SIZE = Constants.MAP_SIZE/staticChunkAmount;
//
//        App.movePlayers(dataCount);
//    }

}