package evascript.test.require;

import dev.kobura.evascript.ScriptEngine;
import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequireTest {

    @Test
    void testRequire() throws LoadingBuildinException, RuntimeError {
        ScriptEngine engine = EngineFactory.createShellEngine().build();
        Scope scope = engine.createScope();
        assertEquals("45", engine.run("@require('bonjour')", scope, null));
    }

}
