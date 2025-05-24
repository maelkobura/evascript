package dev.kobura.evascript.engine.register;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.ContextIdentity;
import dev.kobura.evascript.runtime.context.registerable.RegisteredFunction;
import dev.kobura.evascript.runtime.value.NumberValue;
import dev.kobura.evascript.runtime.value.Value;
import dev.kobura.evascript.runtime.value.ValueType;

import java.util.Map;

public class ExpireFunction implements RegisteredFunction {
    @Override
    public String[] requiredPermissions() {
        return new String[0];
    }

    @Override
    public String name() {
        return "expire";
    }

    @Override
    public Value invoke(Execution execution, Value... values) throws RuntimeError {
        ContextIdentity id = null;
        for(Map.Entry<ContextIdentity, Value> entry : execution.getScope().getPool().entrySet()) {
            if(entry.getValue() == values[0]) {
                id = entry.getKey();
            }
        }
        if(id == null) {
            throw new RuntimeError("Cannot find context identity of " + values[0].toString() + " (it is a variable ?)");
        }
        if(values.length == 2) {
            if(values[1].getType() == ValueType.NUMBER) {
                ContextIdentity newid = new ContextIdentity(id.getName(), id.isConstant(), id.getCreation(), Long.parseLong(values[1].toString()));
                execution.getScope().redefine(id, newid);
                return new NumberValue(newid.getExpiration());
            }
            throw new RuntimeError("Missing new expiration time (it is not an number)");
        }else {
            return new NumberValue(id.getExpiration());
        }
    }
}
