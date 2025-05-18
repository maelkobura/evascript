package evascript.test;

import dev.kobura.evascript.ScriptEngine;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScriptEngineTest {

    @Test
    void testSimpleExpression() throws RuntimeError {
        ScriptEngine engine = ScriptEngine.create().build();
        Scope scope = new Scope(engine);
        assertEquals("Bonjour 45", engine.run("Bonjour {44+1}", scope, null));
    }

    @Test
    void testWithRootExpression() throws RuntimeError, LoadingBuildinException {
        String code = "<?\n" +
                "let cat = \"Bruno\"\n" +
                ">\n" +
                "Your cat name is {cat}.";
        ScriptEngine engine = ScriptEngine.create().build();
        engine.loadDefaultBuildin();
        Scope scope = new Scope(engine);
        assertEquals("Your cat name is Bruno.", engine.run(code, scope, null));
    }

    @Test
    void testFunction() throws RuntimeError, LoadingBuildinException {
        String code = "<?" +
                "func hello() {" +
                "let world = \"Hello world!\";" +
                "return world;" +
                "}>" +
                "Bonjour le monde: {hello()}";
        ScriptEngine engine = ScriptEngine.create().build();
        engine.loadDefaultBuildin();
        Scope scope = new Scope(engine);
        assertEquals("Bonjour le monde: Hello world!", engine.run(code, scope, null));
    }

    @Test
    void testInterop() throws RuntimeError, LoadingBuildinException {
        String code = "Bonjour le monde: {date.now().getDay()}";
        ScriptEngine engine = ScriptEngine.create().build();
        engine.loadDefaultBuildin();
        Scope scope = new Scope(engine);
        assertEquals("Bonjour le monde: 18", engine.run(code, scope, null));
    }

}
