package benchmarks;

import org.openjdk.jmh.annotations.*;
import strategies.App;
import strategies.model.Game;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.SECONDS)
public class Benchmarks {
    @State(Scope.Thread)
    public static class GameCreater{

        @Setup(Level.Trial)
        public void doSetup() {
            app = new App();
            app.createGame(1000, 10, 100, 1, false);
        }

        @TearDown(Level.Trial)
        public void doTearDown() {
            System.out.println("Do TearDown");
        }

        public App app;
    }

    @Benchmark
    public static void createBenchmark() {
        App app = new App();
        app.createGame(1000, 100, 100, 1, false);
    }

    @Benchmark
    public static void changesBenchmark(GameCreater gameCreater) {
        gameCreater.app.changes(1);
    }
}