package dev.kobura.evascript.engine.register;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.registerable.RegisteredFunction;
import dev.kobura.evascript.runtime.value.UndefinedValue;
import dev.kobura.evascript.runtime.value.Value;

public class TypeofFunction implements RegisteredFunction {
    @Override
    public String[] requiredPermissions() {
        return new String[0];
    }

    @Override
    public String name() {
        return "typeof";
    }

    @Override
    public Value invoke(Execution execution, Value... values) throws RuntimeError {
        if(values.length == 1) {
            return Value.from(values[0].getType().toString());
        }
        return UndefinedValue.INSTANCE;
    }
}
