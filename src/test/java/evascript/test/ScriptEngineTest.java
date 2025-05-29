package evascript.test;

import dev.kobura.evascript.engine.ScriptEngine;
import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.engine.ShellEngine;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.ContextIdentity;
import dev.kobura.evascript.runtime.context.Scope;
import dev.kobura.evascript.runtime.value.StringValue;
import dev.kobura.evascript.runtime.value.UndefinedValue;
import evascript.test.sample.SampleObject;
import evascript.test.sample.SampleUser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScriptEngineTest {



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
    void testInjectable() throws LoadingBuildinException, RuntimeError {
        String code = "test.what()";
        ShellEngine engine = EngineFactory.createShellEngine().build();
        engine.loadBuiltin(new SampleObject());
        Scope scope = engine.createScope();
        assertEquals("Hello World!", engine.run(code, scope, null, Map.of("text", "Hello World!")));
    }

    @Test
    void testUserContext() throws LoadingBuildinException, RuntimeError {
        String code = "test.who()";
        ShellEngine engine = EngineFactory.createShellEngine().securityAgent(SampleUser.class).build();
        engine.loadBuiltin(new SampleObject());
        Scope scope = engine.createScope();
        assertEquals("Roberto", engine.run(code, scope, new SampleUser(), Map.of("text", "Hello World!")));
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
    void testExpiration() throws InterruptedException, LoadingBuildinException {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        scope.set(new ContextIdentity("varname", false, Instant.now(), 5), new StringValue("Hello World"));
        Thread.sleep(15);
        scope.refresh();
        assertEquals(UndefinedValue.INSTANCE, scope.get("varname"));
    }

}
