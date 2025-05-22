package evascript.test.engine;

import dev.kobura.evascript.ScriptEngine;
import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import evascript.test.sample.SampleRegister;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShellTest {

    @Test
    void testSimpleExpression() throws RuntimeError, LoadingBuildinException {
        ScriptEngine engine = EngineFactory.createShellEngine().build();
        Scope scope = engine.createScope();
        assertEquals("45", engine.run("44+1", scope, null));
    }

    @Test
    void testSimpleCode() throws RuntimeError, LoadingBuildinException {
        ScriptEngine engine = EngineFactory.createShellEngine().build();
        Scope scope = engine.createScope();
        assertDoesNotThrow(() -> Long.valueOf(engine.run("date.now()", scope, null)));
    }

    @Test
    void testRequireCode() throws RuntimeError, LoadingBuildinException {
        ScriptEngine engine = EngineFactory.createShellEngine().build();
        engine.register(new SampleRegister());
        Scope scope = engine.createScope();
        assertEquals("Hello World!", engine.run("@require('helloworld').hello()", scope, null));
    }

}
