package evascript.test.systems;

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
        engine.register(new evascript.test.sample.SampleRegister());
        assertEquals("Hello World!", engine.run("@require('helloworld').hello()", scope, null));
    }

}
