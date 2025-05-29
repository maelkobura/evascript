package evascript.test.runtime;

import dev.kobura.evascript.engine.ScriptEngine;
import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuntimeTest {


    @Test
    void testSimpleExpression() throws RuntimeError, LoadingBuildinException {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("Bonjour 45", engine.run("Bonjour {44+1}", scope, null));
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
        assertEquals("Error: Division by zero, Message: Execution completed.", engine.run(code, scope, null));
    }

    @Test
    void testSubtractionOperation() throws RuntimeError, LoadingBuildinException {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("20", engine.run("{25 - 5}", scope, null));
    }

    @Test
    void testMultipleVariableOperations() throws RuntimeError, LoadingBuildinException {
        String code = "<? let a = 10; let b = 5; let result = a * b; >Result: {result}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("Result: 50", engine.run(code, scope, null));
    }

    @Test
    void testFunctionWithParameters() throws RuntimeError, LoadingBuildinException {
        String code = "<?" +
                "func addNumbers(a, b) {" +
                "return a + b;" +
                "}>" +
                "Sum: {addNumbers(10, 15)}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("Sum: 25", engine.run(code, scope, null));
    }

    @Test
    void testErrorInMathExpression() throws RuntimeError, LoadingBuildinException {
        String code = "<? try { let x = 5 / 0; } catch (e) { var error = e; } >Error: {error}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("Error: Division by zero", engine.run(code, scope, null));
    }

}
