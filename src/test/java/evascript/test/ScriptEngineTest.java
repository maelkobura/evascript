package evascript.test;

import dev.kobura.evascript.ScriptEngine;
import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.ContextIdentity;
import dev.kobura.evascript.runtime.context.Scope;
import dev.kobura.evascript.runtime.value.StringValue;
import dev.kobura.evascript.runtime.value.UndefinedValue;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScriptEngineTest {

    @Test
    void testSimpleExpression() throws RuntimeError, LoadingBuildinException {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("Bonjour 45", engine.run("Bonjour {44+1}", scope, null));
    }

    @Test
    void testSampleCode() throws RuntimeError, IOException, LoadingBuildinException {
        File file = new File("src/test/resources/sample.txt");
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("76", engine.run(file, scope, null));
    }

    @Test
    void testDataObject() throws RuntimeError, IOException, LoadingBuildinException {
        String code = "<?var z = {world: \"World\"}; var i = {hello: {[\"hello\"]: \"Hello\"}, ...z};> {i.hello.hello}{i.world}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("HelloWorld", engine.run(code, scope, null));
    }

    @Test
    void testWithRootExpression() throws RuntimeError, LoadingBuildinException {
        String code = "<?\n" +
                "let cat = \"Bruno\";" +
                ">\n" +
                "Your cat name is {cat}.";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
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
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("Bonjour le monde: Hello world!", engine.run(code, scope, null));
    }

    @Test
    void testInterop() throws RuntimeError, LoadingBuildinException {
        String code = "Bonjour le monde: {date.now().getDay()}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("Bonjour le monde: " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH), engine.run(code, scope, null));
    }

    @Test
    void testTryCatch() throws RuntimeError, LoadingBuildinException {
        String code = "<? try{date.create(\"mouahahah\");} catch(e) {var error = e;} > L'erreur: {error}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("L'erreur: Native method 'create' threw an exception: argument type mismatch", engine.run(code, scope, null));
    }


    @Test
    void testTryCatchFinally() throws RuntimeError, LoadingBuildinException {
        String code = "<? try{let x = 10 / 0;} catch(e) {var error = e;} finally {var message = \"Execution completed.\";} > Error: {error}, Message: {message}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        engine.run(code, scope, null);
        assertEquals("Error: Division by zero, Message: Execution completed.", engine.run(code, scope, null));
    }

    @Test
    void testExpiration() throws InterruptedException, LoadingBuildinException {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        scope.set(new ContextIdentity("varname", false, Instant.now(), 5), new StringValue("Hello World"));
        Thread.sleep(15);
        scope.refresh();
        assertEquals(UndefinedValue.INSTANCE, scope.get("varname"));
    }

}
