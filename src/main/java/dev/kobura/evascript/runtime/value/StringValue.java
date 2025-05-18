package dev.kobura.evascript.runtime.value;

import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.security.PermissiveUser;

public class StringValue extends Value {

    private String val;

    public StringValue(String val) {
        this.val = val;
    }

    @Override
    public Object unwrap() {
        return val;
    }

    @Override
    public ValueType getType() {
        return ValueType.STRING;
    }

    @Override
    public boolean isEqual(Value other) {
        return other.toString().equals(val);
    }

    @Override
    public String toString() {
        return val;
    }

    @Override
    public Value getField(Execution execution, PermissiveUser user, String name) {

        //No permission required

        if(name.equals("length")) {
            return new NumberValue(val.length());
        }
        return UndefinedValue.INSTANCE;
    }

    @Override
    public Value add(Value other) {
        return new StringValue(val + other.toString());
    }
}
