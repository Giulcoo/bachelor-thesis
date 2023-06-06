package benchmarks;

import org.openjdk.jmh.annotations.*;
import strategies.App;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.SECONDS)
public class Benchmarks {
    @Benchmark
    public static void mainBenchmark() {
        App app = new App();
        app.startGame(1, 1, 1, 1, false);
    }
}