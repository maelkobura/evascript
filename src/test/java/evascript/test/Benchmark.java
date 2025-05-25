package evascript.test;

import dev.kobura.evascript.ScriptEngine;
import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class Benchmark {

    @Test
    void benchmark() throws RuntimeError, LoadingBuildinException {
        String code = "<? try{let x = 10 / 0;} catch(e) {var error = e;} finally {var message = \"Execution completed.\";} > Error: {error}, Message: {message}";

        // Create engine and load default built-ins
        ScriptEngine engine = EngineFactory.createRootedEngine().build();

        // Benchmark the code snippet
        long totalExecutionTime = 0;
        int iterations = 100;

        for (int i = 0; i < iterations; i++) {
            Scope scope = engine.createScope();
            long start = System.nanoTime();
            engine.run(code, scope, null);
            long end = System.nanoTime();
            totalExecutionTime += (end - start);
        }

        double averageTime = (double) totalExecutionTime / iterations / 1_000_000.0;
        System.out.println("Average execution time (milliseconds): " + averageTime);
    }



}
