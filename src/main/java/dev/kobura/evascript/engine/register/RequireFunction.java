package dev.kobura.evascript.engine.register;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.Scriptable;
import dev.kobura.evascript.runtime.context.registerable.Register;
import dev.kobura.evascript.runtime.context.registerable.RegisteredFunction;
import dev.kobura.evascript.runtime.value.NullValue;
import dev.kobura.evascript.runtime.value.UndefinedValue;
import dev.kobura.evascript.runtime.value.Value;

public class RequireFunction implements RegisteredFunction {
    @Override
    public String[] requiredPermissions() {
        return new String[0];
    }

    @Override
    public String name() {
        return "require";
    }

    @Override
    public Value invoke(Execution execution, Value... values) throws RuntimeError {
        String value = values[0].toString();
        for(Register reg : execution.getEngine().getRegisters()) {
            Object obj = reg.require(value);
            if(obj != null && obj.getClass().isAnnotationPresent(Scriptable.class)) {
                return Value.from(obj);
            }
        }
        return UndefinedValue.INSTANCE;
    }
}
