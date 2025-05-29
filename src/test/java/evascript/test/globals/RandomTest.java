package evascript.test.globals;

import dev.kobura.evascript.engine.ScriptEngine;
import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomTest {

    @Test
    void testNextInt() throws RuntimeError, LoadingBuildinException {
        String code = "{random.nextInt(10)}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        String result = engine.run(code, scope, null);
        int value = Integer.parseInt(result);
        assertTrue(value >= 0 && value < 10, "random.nextInt(10) should return value between 0 and 9");
    }

    @Test
    void testNextDouble() throws RuntimeError, LoadingBuildinException {
        String code = "{random.nextDouble()}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        String result = engine.run(code, scope, null);
        double value = Double.parseDouble(result);
        assertTrue(value >= 0.0 && value < 1.0, "random.nextDouble() should return value between 0.0 and 1.0");
    }

    @Test
    void testNextBoolean() throws RuntimeError, LoadingBuildinException {
        String code = "{random.nextBoolean()}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        String result = engine.run(code, scope, null);
        assertTrue(result.equals("true") || result.equals("false"), "random.nextBoolean() should return 'true' or 'false'");
    }
}