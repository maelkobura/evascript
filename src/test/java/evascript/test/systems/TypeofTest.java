package evascript.test.systems;

import dev.kobura.evascript.ScriptEngine;
import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeofTest {

    @Test
    void testNumberTypeof() throws LoadingBuildinException, RuntimeError {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        String code = "<? var i = 45 > {@typeof(i)}";
        assertEquals("number", engine.run(code, scope, null));
    }

    @Test
    void testStringTypeof() throws LoadingBuildinException, RuntimeError {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        String code = "<? var i = \"Hello\" > {@typeof(i)}";
        assertEquals("string", engine.run(code, scope, null));
    }

    @Test
    void testBooleanTypeof() throws LoadingBuildinException, RuntimeError {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        String code = "<? var i = true > {@typeof(i)}";
        assertEquals("boolean", engine.run(code, scope, null));
    }

    @Test
    void testObjectTypeof() throws LoadingBuildinException, RuntimeError {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        String code = "<? var i = {key: \"value\"} > {@typeof(i)}";
        assertEquals("object", engine.run(code, scope, null));
    }

    @Test
    void testArrayTypeof() throws LoadingBuildinException, RuntimeError {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        String code = "<? var i = [1, 2, 3] > {@typeof(i)}";
        assertEquals("array", engine.run(code, scope, null));
    }

    @Test
    void testFunctionTypeof() throws LoadingBuildinException, RuntimeError {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        String code = "<? var i = func() { return 1; } > {@typeof(i)}";
        assertEquals("function", engine.run(code, scope, null));
    }

    @Test
    void testNullTypeof() throws LoadingBuildinException, RuntimeError {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        String code = "<? var i = null > {@typeof(i)}";
        assertEquals("null", engine.run(code, scope, null));
    }

    @Test
    void testUndefinedTypeof() throws LoadingBuildinException, RuntimeError {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        String code = "<? var i > {@typeof(i)}";
        assertEquals("undefined", engine.run(code, scope, null));
    }
    
    

}
