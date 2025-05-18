package dev.kobura.evascript.runtime.value;


public class UndefinedValue extends Value {
    public static final UndefinedValue INSTANCE = new UndefinedValue();

    private UndefinedValue() {} // Private constructor

    @Override
    public Object unwrap() {
        return null; // undefined has no direct Java equivalent
    }

    @Override
    public ValueType getType() {
        return ValueType.UNDEFINED;
    }

    @Override
    public boolean isEqual(Value other) {
        return false;
    }

    @Override
    public String toString() {
        return "undefined";
    }
}
