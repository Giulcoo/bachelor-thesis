package benchmarks;

import org.openjdk.jmh.annotations.*;
import strategies.App;
import strategies.Constants;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 0)
@Measurement(iterations = 1)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class DynamicBenchmark {
    @Param({"true", "false"})
    public static boolean useChangeFile;
    @Param({"true", "false"})
    public static boolean useGzip;
    @Param({"1000", "10000", "100000"})
    public static int dataCount;
    @Param({"1000", "5000", "10000"})
    public static int chunkMaxElements;
    @Param({"2", "10", "100"})
    public static int chunkMinElements;

    @State(Scope.Benchmark)
    public static class CreateData {
        @Setup(Level.Iteration)
        public void setUp() {
            App.createGame(dataCount);
        }
    }

    @Benchmark
    public static void createGame(){
        Constants.DYNAMIC_CHUNK_SIZE = true;
        Constants.USE_CHANGE_FILE = useChangeFile;
        Constants.USE_GZIP = useGzip;
        Constants.CHUNK_MAX_ELEMENTS = chunkMaxElements;
        Constants.CHUNK_GROUP_MIN_ELEMENTS = chunkMaxElements/chunkMinElements;

        App.createGame(dataCount);
    }

    @Benchmark
    public static void loadGame(CreateData createData){
        Constants.DYNAMIC_CHUNK_SIZE = true;
        Constants.USE_CHANGE_FILE = useChangeFile;
        Constants.USE_GZIP = useGzip;
        Constants.CHUNK_MAX_ELEMENTS = chunkMaxElements;
        Constants.CHUNK_GROUP_MIN_ELEMENTS = chunkMaxElements/chunkMinElements;

        App.loadGame();
    }

    @Benchmark
    public static void createPlayers(CreateData createData){
        Constants.DYNAMIC_CHUNK_SIZE = true;
        Constants.USE_CHANGE_FILE = useChangeFile;
        Constants.USE_GZIP = useGzip;
        Constants.CHUNK_MAX_ELEMENTS = chunkMaxElements;
        Constants.CHUNK_GROUP_MIN_ELEMENTS = chunkMaxElements/chunkMinElements;

        App.createPlayers(dataCount);
    }

    @Benchmark
    public static void removePlayers(CreateData createData){
        Constants.DYNAMIC_CHUNK_SIZE = true;
        Constants.USE_CHANGE_FILE = useChangeFile;
        Constants.USE_GZIP = useGzip;
        Constants.CHUNK_MAX_ELEMENTS = chunkMaxElements;
        Constants.CHUNK_GROUP_MIN_ELEMENTS = chunkMaxElements/chunkMinElements;

        App.removePlayers(dataCount-10);
    }

    @Benchmark
    public static void movePlayers(CreateData createData){
        Constants.DYNAMIC_CHUNK_SIZE = true;
        Constants.USE_CHANGE_FILE = useChangeFile;
        Constants.USE_GZIP = useGzip;
        Constants.CHUNK_MAX_ELEMENTS = chunkMaxElements;
        Constants.CHUNK_GROUP_MIN_ELEMENTS = chunkMaxElements/chunkMinElements;

        App.movePlayers(dataCount);
    }
}