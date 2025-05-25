package evascript.test.runtime;

import dev.kobura.evascript.ScriptEngine;
import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatementTest {

    @Test
    void foreachTest() throws RuntimeError, LoadingBuildinException {
        String code = "<? var i = [14, 56, 78]; var e = 0; for(let z : i) e=e+z; > {e}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("148", engine.run(code, scope, null));
    }

    @Test
    void whileTest() throws RuntimeError, LoadingBuildinException {
        String code = "<? var i = 0; while(i<10) {i=i+1;} > {i}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("10", engine.run(code, scope, null));
    }

    @Test
    void ifTest() throws RuntimeError, LoadingBuildinException {
        String code = "<? var i = 0; if(i<10) {i=i+1;} else {i=i-1;} > {i}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("1", engine.run(code, scope, null));
    }

    @Test
    void breakTest() throws RuntimeError, LoadingBuildinException {
        String code = "<? var i = 0; while(true) {if(i==10) break; i=i+1;} > {i}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("10", engine.run(code, scope, null));
    }

    @Test
    void continueTest() throws RuntimeError, LoadingBuildinException {
        String code = "<? var i = 0; var c = 0; while(i < 10) { if(i == 5) { i = i + 1; continue; } c = c + 1; i = i + 1; } > {c}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("9", engine.run(code, scope, null));
    }

    @Test
    void doWhileTest() throws RuntimeError, LoadingBuildinException {
        String code = "<? var i = 0; do {i=i+1;} while(i<10); > {i}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("10", engine.run(code, scope, null));
    }

    @Test
    void switchTest() throws RuntimeError, LoadingBuildinException {
        String code = "<? var i = 3; var result; switch(i) { case 1: result = 'one'; break; case 2: result = 'two'; break; case 3: result = 'three'; break; default: result = 'unknown'; } > {result}";
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        assertEquals("three", engine.run(code, scope, null));
    }


}
