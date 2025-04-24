package gg.kobuz.evascript.runtime;

import gg.kobuz.evascript.ScriptEngine;
import gg.kobuz.evascript.errors.RuntimeError;
import gg.kobuz.evascript.runtime.context.ContextObject;
import gg.kobuz.evascript.runtime.context.VariableObject;
import gg.kobuz.evascript.runtime.parsing.ast.expression.context.variable.args.ArgumentExpression;

import java.util.Map;

public class Execution {

    private Execution parent;
    private ScriptEngine engine;
    private Map<String, VariableObject> variables;

    public Object var(String name) {
        if(variables.containsKey(name)) {
            return variables.get(name);
        }
        if(parent != null) {
            return parent.var(name);
        }
        return null;
    }

    public void var(String name, Object value, boolean local) {

    }


}
