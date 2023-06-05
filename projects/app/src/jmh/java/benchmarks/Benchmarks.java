package benchmarks;

import org.openjdk.jmh.annotations.*;
import strategies.App;

import java.util.concurrent.TimeUnit;

@Fork(value = 0, warmups = 0)
@Warmup(iterations = 0)
@Measurement(iterations = 1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class Benchmarks {
    @Benchmark
    public static void mainBenchmark() {
        App app = new App();
        app.startGame(1, 1, 1, 1, false);
    }
}