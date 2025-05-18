package dev.kobura.evascript.runtime.value;

public class NaNValue extends Value {
    public static final NaNValue INSTANCE = new NaNValue();

    private NaNValue() {} // Private constructor

    @Override
    public Object unwrap() {
        return Double.NaN;
    }

    @Override
    public ValueType getType() {
        return ValueType.NUMBER;
    }

    @Override
    public boolean isEqual(Value other) {
        return false;
    }

    @Override
    public String toString() {
        return "NaN";
    }

}