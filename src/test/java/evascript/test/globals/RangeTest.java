package evascript.test.globals;

import dev.kobura.evascript.ScriptEngine;
import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RangeTest {


    private String execute(String code) throws RuntimeError, LoadingBuildinException {
        ScriptEngine engine = EngineFactory.createRootedEngine().build();
        Scope scope = engine.createScope();
        return engine.run(code, scope, null);
    }

    @Test
    void testRangeDefaultStep() throws RuntimeError, LoadingBuildinException {
        String code = "<? var r = @range(0, 5); >{r}"; // [0,1,2,3,4]
        assertEquals("[0, 1, 2, 3, 4]", execute(code));
    }

    @Test
    void testRangeWithStep() throws RuntimeError, LoadingBuildinException {
        String code = "<? var r = @range(0, 10, 2); >{r}";
        assertEquals("[0, 2, 4, 6, 8]", execute(code));
    }

    @Test
    void testRangeElementAccess() throws RuntimeError, LoadingBuildinException {
        String code = "<? var r = @range(0, 5); >{r[2]}";
        assertEquals("2", execute(code));
    }

    @Test
    void testRangeNegativeStep() throws RuntimeError, LoadingBuildinException {
        String code = "<? var r = @range(5, 0, -1); >{r}";
        assertEquals("[5, 4, 3, 2, 1]", execute(code));
    }

    @Test
    void testRangeEmptyWhenEndEqualsStart() throws RuntimeError, LoadingBuildinException {
        String code = "<? var r = @range(3, 3); >{r}";
        assertEquals("[]", execute(code));
    }

    @Test
    void testRangeEmptyWhenStepWrongDirection() throws RuntimeError, LoadingBuildinException {
        String code = "<? var r = @range(0, 5, -1); >{r}";
        assertEquals("[]", execute(code));
    }

    @Test
    void testRangeSingleElement() throws RuntimeError, LoadingBuildinException {
        String code = "<? var r = @range(2, 3); >{r}";
        assertEquals("[2]", execute(code));
    }

    @Test
    void testRangeStepGreaterThanDiff() throws RuntimeError, LoadingBuildinException {
        String code = "<? var r = @range(0, 5, 10); >{r}";
        assertEquals("[0]", execute(code));
    }

}
