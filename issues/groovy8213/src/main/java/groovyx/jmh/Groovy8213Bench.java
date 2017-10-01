package groovyx.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class Groovy8213Bench {

    private boolean initialized;
    private volatile boolean initializedVolatile;
    private boolean initializedChecked = true;

    @Benchmark
    public boolean read_1_normal() {
        return initialized;
    }

    @Benchmark
    public boolean read_2_volatile() {
        return initializedVolatile;
    }

    @Benchmark
    public boolean read_3_check_with_sync_1() {
        if (!initializedChecked) {
            synchronized (this) {
                return initializedChecked;
            }
        }
        return true;
    }

    @Benchmark
    public boolean read_4_check_with_sync_2() {
        if (initializedChecked) {
            return true;
        }
        synchronized (this) {
            return initializedChecked;
        }
    }

}
