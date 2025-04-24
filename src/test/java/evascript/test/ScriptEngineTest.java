package evascript.test;

import gg.kobuz.evascript.ScriptEngine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScriptEngineTest {

    @Test
    void testSimpleRun() {
        assertEquals("caca", ScriptEngine.create().run("1+3"));
    }

}
