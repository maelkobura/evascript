package evascript.test.runtime;

import dev.kobura.evascript.ScriptEngine;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayTest {

    @Test
    void arrayTest() throws RuntimeError {
        String code = "<? var i = [14, 56, 78]; > {i[0]}";
        ScriptEngine engine = ScriptEngine.create().build();
        Scope scope = new Scope(engine);
        assertEquals("14", engine.run(code, scope, null));
    }

    @Test
    void arrayAppendTest() throws RuntimeError {
        String code = "<? var i = [14, 56, 78]; i.append(45); > {i[3]}";
        ScriptEngine engine = ScriptEngine.create().build();
        Scope scope = new Scope(engine);
        assertEquals("45", engine.run(code, scope, null));
    }

    @Test
    void arraySetTest() throws RuntimeError {
        String code = "<? var i = [14, 56, 78]; i.set(1, 45); > {i[1]}";
        ScriptEngine engine = ScriptEngine.create().build();
        Scope scope = new Scope(engine);
        assertEquals("45", engine.run(code, scope, null));
    }

    @Test
    void arrayFunctionForeachTest() throws RuntimeError {
        String code = "<? var i = [14, 56, 78]; var z = []; i.foreach(func (a) { z.append(a + 20); }); > {z[1]}";
        ScriptEngine engine = ScriptEngine.create().build();
        Scope scope = new Scope(engine);
        assertEquals("76", engine.run(code, scope, null));
    }

    @Test
    void arrayForeachTest() throws RuntimeError {
        String code = "<? " +
                "var i = [14, 56, 78]; " +
                "var error = null;" +
                "var z = []; " +
                "for(let a:i) { " +
                "z.append(a + 20); " +
                "} " +
                "> {z[1]}";
        ScriptEngine engine = ScriptEngine.create().build();
        Scope scope = new Scope(engine);
        assertEquals("76", engine.run(code, scope, null));
    }

}
