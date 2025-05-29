package evascript.test.globals;

import dev.kobura.evascript.engine.ScriptEngine;
import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MathTest {

    @Test
    void testFloor() throws RuntimeError, LoadingBuildinException {
        String code = "{math.floor(3.7)}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("3", engine.run(code, scope, null));
    }

    @Test
    void testCeil() throws RuntimeError, LoadingBuildinException {
        String code = "{math.ceil(3.2)}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("4", engine.run(code, scope, null));
    }

    @Test
    void testRound() throws RuntimeError, LoadingBuildinException {
        String code = "{math.round(3.6)}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("4", engine.run(code, scope, null));
    }

    @Test
    void testSqrt() throws RuntimeError, LoadingBuildinException {
        String code = "{math.sqrt(9)}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("3", engine.run(code, scope, null));
    }

    @Test
    void testPow() throws RuntimeError, LoadingBuildinException {
        String code = "{math.pow(2, 3)}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("8", engine.run(code, scope, null));
    }

    @Test
    void testSqrtNegative() throws LoadingBuildinException {
        String code = "{math.sqrt(-4)}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        RuntimeError error = assertThrows(RuntimeError.class, () -> engine.run(code, scope, null));
        assertEquals("Cannot compute square root of a negative number", error.getMessage());
    }
}
