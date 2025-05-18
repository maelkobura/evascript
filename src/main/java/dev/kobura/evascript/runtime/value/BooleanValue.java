package dev.kobura.evascript.runtime.value;

public class BooleanValue extends Value {

    private boolean value;

    public BooleanValue(Boolean v) {
        this.value = v;
    }

    @Override
    public Object unwrap() {
        return value;
    }

    @Override
    public ValueType getType() {
        return ValueType.BOOLEAN;
    }

    @Override
    public boolean isEqual(Value other) {
        return other instanceof BooleanValue && ((BooleanValue) other).value == value;
    }

    public static boolean isTruthy(Value value) {
        if (value instanceof BooleanValue) {
            return ((Boolean) value.unwrap()).booleanValue();
        }
        if (value.isEqual(NullValue.INSTANCE) ||
                value.isEqual(UndefinedValue.INSTANCE) ||
                value.isEqual(NaNValue.INSTANCE)) {
            return false;
        }
        if (value instanceof NumberValue) {
            double num = ((Number) value.unwrap()).doubleValue();
            return num != 0;
        }
        if (value instanceof StringValue) {
            return !((String) value.unwrap()).isEmpty();
        }
        return true;  // Arrays, Objects, etc. are truthy
    }

}
