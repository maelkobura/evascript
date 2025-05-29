package evascript.test.systems;

import dev.kobura.evascript.engine.ScriptEngine;
import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpirationTest {

    @Test
    void testDisplayExpiration() throws LoadingBuildinException, RuntimeError {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        String code = "<? var i = 45 > {@expire(i)}";
        assertEquals("600000", engine.run(code, scope, null));
    }

    @Test
    void testModifyExpiration() throws LoadingBuildinException, RuntimeError {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        String code = "<? var i = 45; @expire(i, 900000) > {@expire(i)}";
        assertEquals("900000", engine.run(code, scope, null));
    }

}
