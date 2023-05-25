package benchmarks;

import org.openjdk.jmh.annotations.*;
import strategies.App;

import java.util.concurrent.TimeUnit;

@Fork(value = 0, warmups = 1)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
@OutputTimeUnit(TimeUnit.SECONDS)
public class Benchmarks {
    @Benchmark
    public static void mainBenchmark() {
        App app = new App();
        app.startGame(1, 1, 1, 1, false);
    }
}