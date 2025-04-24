package gg.kobuz.evascript;

import gg.kobuz.evascript.runtime.context.ContextObject;
import lombok.SneakyThrows;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ast.Scope;

import java.util.HashMap;
import java.util.Map;

public class ScriptEngine {

    private final boolean throwdable;
    private Map<String, ContextObject> globals;

    private ScriptEngine(boolean throwdable) {
        this.throwdable = throwdable;
        this.globals = new HashMap<>();
    }

    public ContextObject getGlobal(String name) {
        return globals.getOrDefault(name, null);
    }

    public static ScriptEngine create() {
        return new ScriptEngine(false);
    }

    public static ScriptEngine createThrowdable() {
        return new ScriptEngine(true);
    }

    public static ScriptEngine create(boolean throwdable) {
        return new ScriptEngine(throwdable);
    }

    /**
     * Checks if this script engine is throwdable, i.e. if exceptions are raised
     * as a result of the script execution.
     *
     * @return True if this script engine is throwdable, false otherwise.
     */
    public boolean isThrowdable() {
        return throwdable;
    }

    @SneakyThrows
    public String run(String code) {

        Context ctx = Context.enter();
        Scriptable scope = ctx.initStandardObjects();
        scope.


        return "caca";
    }





}
