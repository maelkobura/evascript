package evascript.test.runtime;

import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.engine.ScriptEngine;
import dev.kobura.evascript.engine.ShellEngine;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import evascript.test.sample.SampleObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArgumentTest {

    @Test
    void sequencedArgs() throws RuntimeError, LoadingBuildinException {
        ShellEngine engine = EngineFactory.createShellEngine().build();
        engine.loadBuiltin(new SampleObject());
        String code = "test.where(\"Hello\", \" Wor\", \"ld\")";
        Scope scope = engine.createScope();
        assertEquals("Hello World", engine.run(code, scope, null));
    }

    @Test
    void completeStructuredArgs() throws RuntimeError, LoadingBuildinException {
        ShellEngine engine = EngineFactory.createShellEngine().build();
        engine.loadBuiltin(new SampleObject());
        String code = "test.where(first=\"Hello\",second=\" Wor\",third=\"ld\")";
        Scope scope = engine.createScope();
        assertEquals("Hello World", engine.run(code, scope, null));
    }

    @Test
    void missingThirdStructuredArgs() throws RuntimeError, LoadingBuildinException {
        ShellEngine engine = EngineFactory.createShellEngine().build();
        engine.loadBuiltin(new SampleObject());
        String code = "test.where(first=\"Hello\",second=\" World\")";
        Scope scope = engine.createScope();
        assertEquals("Hello World", engine.run(code, scope, null));
    }

    @Test
    void missingSecondStructuredArgs() throws RuntimeError, LoadingBuildinException {
        ShellEngine engine = EngineFactory.createShellEngine().build();
        engine.loadBuiltin(new SampleObject());
        String code = "test.where(first=\"Hello\",third=\" World\")";
        Scope scope = engine.createScope();
        assertEquals("Hello World", engine.run(code, scope, null));
    }

    @Test
    void arrayArgs() throws RuntimeError, LoadingBuildinException {
        ShellEngine engine = EngineFactory.createShellEngine().build();
        engine.loadBuiltin(new SampleObject());
        String code = "test.how([\"Hello\", \"World\"])";
        Scope scope = engine.createScope();
        assertEquals("Hello World", engine.run(code, scope, null));
    }

    @Test
    void structuredArrayArgs() throws RuntimeError, LoadingBuildinException {
        ShellEngine engine = EngineFactory.createShellEngine().build();
        engine.loadBuiltin(new SampleObject());
        String code = "test.how(args=[\"Hello\", \"World\"])";
        Scope scope = engine.createScope();
        assertEquals("Hello World", engine.run(code, scope, null));
    }

}
