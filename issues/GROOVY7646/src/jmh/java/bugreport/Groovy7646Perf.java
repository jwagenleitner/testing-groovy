package bugreport;

import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyShell;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Date;

/**
 * Performance test the changes proposed https://github.com/apache/groovy/pull/325.
 * Assumption is that the size of the script parsed or the complexity of the
 * logic does not impact the performance.  What we are testing is the call
 * to ClassInfo#removeClassInfo and it's impact on the GroovyShell#evaluate
 * method runtime.
 */
public class Groovy7646Perf {

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    public Integer shellEvaluate() {
        long time = new Date().getTime();
        GroovyCodeSource gcs = new GroovyCodeSource("40 + 2 // " + time, "Script1.groovy", GroovyShell.DEFAULT_CODE_BASE);
        GroovyShell shell = new GroovyShell();
        Integer result = (Integer) shell.evaluate(gcs);
        return result;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Groovy7646Perf.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}