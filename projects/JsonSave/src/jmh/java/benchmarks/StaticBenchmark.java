package benchmarks;

import org.openjdk.jmh.annotations.*;
import strategies.App;
import strategies.Constants;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
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
        @Setup(Level.Invocation)
        public void setUp() {
            Constants.DYNAMIC_CHUNK_SIZE = false;
            Constants.USE_CHANGE_FILE = useChangeFile;
            Constants.USE_GZIP = useGzip;
            Constants.STATIC_CHUNK_AMOUNT = staticChunkAmount;
            Constants.STATIC_CHUNK_SIZE = Constants.MAP_SIZE/staticChunkAmount;

            App.createGame(dataCount+10);
        }
    }

    @State(Scope.Benchmark)
    public static class CreateSmallData {
        @Setup(Level.Invocation)
        public void setUp() {
            Constants.DYNAMIC_CHUNK_SIZE = false;
            Constants.USE_CHANGE_FILE = useChangeFile;
            Constants.USE_GZIP = useGzip;
            Constants.STATIC_CHUNK_AMOUNT = staticChunkAmount;
            Constants.STATIC_CHUNK_SIZE = Constants.MAP_SIZE/staticChunkAmount;

            App.createGame(100);
        }
    }

//    @Benchmark
//    public static void createGame(){
//        Constants.DYNAMIC_CHUNK_SIZE = false;
//        Constants.USE_CHANGE_FILE = useChangeFile;
//        Constants.USE_GZIP = useGzip;
//        Constants.STATIC_CHUNK_AMOUNT = staticChunkAmount;
//        Constants.STATIC_CHUNK_SIZE = Constants.MAP_SIZE/staticChunkAmount;
//
//        App.createGame(dataCount);
//    }
//
//    @Benchmark
//    public static void loadGame(CreateData createData){
//        Constants.DYNAMIC_CHUNK_SIZE = false;
//        Constants.USE_CHANGE_FILE = useChangeFile;
//        Constants.USE_GZIP = useGzip;
//        Constants.STATIC_CHUNK_AMOUNT = staticChunkAmount;
//        Constants.STATIC_CHUNK_SIZE = Constants.MAP_SIZE/staticChunkAmount;
//
//        App.loadGame();
//    }

//    @Benchmark
//    public static void createPlayers(CreateSmallData createData){
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
//        App.removePlayers(dataCount);
//    }
//
//    @Benchmark
//    public static void movePlayers(CreateSmallData createData){
//        Constants.DYNAMIC_CHUNK_SIZE = false;
//        Constants.USE_CHANGE_FILE = useChangeFile;
//        Constants.USE_GZIP = useGzip;
//        Constants.STATIC_CHUNK_AMOUNT = staticChunkAmount;
//        Constants.STATIC_CHUNK_SIZE = Constants.MAP_SIZE/staticChunkAmount;
//
//        App.movePlayers(dataCount);
//    }

}