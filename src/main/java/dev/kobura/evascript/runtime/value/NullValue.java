package dev.kobura.evascript.runtime.value;

public class NullValue extends Value {
    public static final NullValue INSTANCE = new NullValue();

    private NullValue() {} // Private constructor to ensure singleton pattern

    @Override
    public Object unwrap() {
        return null;
    }

    @Override
    public ValueType getType() {
        return ValueType.NULL;
    }

    @Override
    public boolean isEqual(Value other) {
        return false;
    }

    @Override
    public String toString() {
        return "null";
    }
}
