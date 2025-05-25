package evascript.test.runtime;

import dev.kobura.evascript.ScriptEngine;
import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import org.junit.jupiter.api.Test;

public class TypeTest {

    @Test
    void testDouble() throws LoadingBuildinException, RuntimeError {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        System.out.println(engine.run("{1.0}", scope, null));
    }

}
