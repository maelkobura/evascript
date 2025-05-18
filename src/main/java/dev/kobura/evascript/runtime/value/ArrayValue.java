package dev.kobura.evascript.runtime.value;

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

}
