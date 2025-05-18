package dev.kobura.evascript.runtime.value;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.Scriptable;
import dev.kobura.evascript.security.PermissiveUser;

public abstract class Value {

    public abstract Object unwrap();
    public abstract ValueType getType();
    public abstract boolean isEqual(Value other);

    public String toString() {
        return getType().toString();
    };

    public Value getField(Execution execution, PermissiveUser user, String name) throws RuntimeError {
        return UndefinedValue.INSTANCE;
    }
    public void setField(Execution execution, PermissiveUser user, String name, Value value) throws RuntimeError {
        throw new RuntimeError("Cannot set property '" + name + "' of " + getType().toString());
    }

    public Value execute(Execution execution, PermissiveUser user, String methodName, Value args) throws RuntimeError {
        throw new RuntimeError("Cannot execute method '" + methodName + "' of " + getType().toString());
    }

    public Value execute(Execution execution, PermissiveUser user, Value args) throws RuntimeError {
        throw new RuntimeError("Cannot execute the object " + getType().toString());
    }

    public Value add(Value other)throws RuntimeError {
        return NaNValue.INSTANCE;
    }
    public Value subtract(Value other) throws RuntimeError {
        return NaNValue.INSTANCE;
    }
    public Value multiply(Value other)throws RuntimeError {
        return NaNValue.INSTANCE;
    }
    public Value divide(Value other)throws RuntimeError {
        return NaNValue.INSTANCE;
    }
    public Value modulo(Value other)throws RuntimeError {
        return NaNValue.INSTANCE;
    }

    public static Value from(Object o) {
        if (o instanceof String stringValue) {
            return new StringValue(stringValue);
        } else if (o instanceof Number numberValue) {
            return new NumberValue(numberValue.doubleValue());
        } else if (o instanceof Boolean booleanValue) {
            return new BooleanValue(booleanValue);
        } else if (o.getClass().isAnnotationPresent(Scriptable.class)) {
            return new JavaObjectValue(o);
        } else if (o instanceof Value value) {
            return value;
        } else if (o instanceof Object[]) {
            // TODO: Handle arrays
            return UndefinedValue.INSTANCE;
        } else {
            return NullValue.INSTANCE;
        }
    }

}
