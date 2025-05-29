package evascript.test.runtime;

import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.engine.ScriptEngine;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataTest {

    //TODO

    @Test
    void keysFunction() throws RuntimeError, LoadingBuildinException {
        ScriptEngine engine = EngineFactory.createShellEngine().build();
        String code = "let a = {key1:45,key2:23,key3:12}; a.keys();";
        Scope scope = engine.createScope();
        assertEquals("[key1, key2, key3]", engine.run(code, scope, null));
    }

    @Test
    void valuesFunction() throws RuntimeError, LoadingBuildinException {
        ScriptEngine engine = EngineFactory.createShellEngine().build();
        String code = "let a = {key1:45,key2:23,key3:12}; a.values();";
        Scope scope = engine.createScope();
        assertEquals("[45, 23, 12]", engine.run(code, scope, null));
    }
    
    

}
