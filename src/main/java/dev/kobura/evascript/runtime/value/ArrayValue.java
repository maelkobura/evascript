package dev.kobura.evascript.runtime.value;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.ContextData;
import dev.kobura.evascript.runtime.context.Scriptable;
import dev.kobura.evascript.security.PermissiveUser;

import java.util.List;

public class ArrayValue extends Value{

    private List<Value> values;

    public ArrayValue(List<Value> values) {
        this.values = values;
    }

    public List<Value> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return values.toString();
    }

    @Override
    public Object unwrap() {
        return values.stream().map(Value::unwrap).toList();
    }

    @Override
    public ValueType getType() {
        return ValueType.ARRAY;
    }

    @Override
    public boolean isEqual(Value other) {
        if (other instanceof ArrayValue lv) {
            return values.equals(lv.values);
        }
        return false;
    }

    @Override
    public Value execute(Execution execution, PermissiveUser user, String methodName, Value args) throws RuntimeError {
        return super.execute(execution, user, methodName, args);
    }

    @Scriptable
    public Value set(int index, Object obj) {
        Value v = Value.from(obj);
        values.add(index, v);
        return v;
    }

    @Scriptable
    public Value append(Value v) {
        values.add(v);
        return v;
    }

    @Scriptable
    public void foreach(@ContextData Execution execution, @ContextData PermissiveUser user, FunctionValue func) throws RuntimeError {
        for(Value v : values) {
            func.execute(execution, user, new ArrayValue(List.of(v)));
        }
    }
}
