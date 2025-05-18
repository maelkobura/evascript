package dev.kobura.evascript.runtime.value;

import dev.kobura.evascript.errors.RuntimeError;

public class NumberValue extends Value {

    private double val;

    public NumberValue(double val) {
        this.val = val;
    }

    @Override
    public Object unwrap() {
        if (val == ((Number) val).intValue()) {
            return (int) val;
        }
        return val;
    }


    @Override
    public ValueType getType() {
        return ValueType.NUMBER;
    }

    @Override
    public boolean isEqual(Value other) {
        if(!(other instanceof NumberValue)) return false;
        return val == ((int) other.unwrap());
    }

    @Override
    public Value add(Value other) throws RuntimeError {

        if(other.getType() == ValueType.STRING) {
            return new StringValue(val + other.toString());
        }else if(other.getType() == ValueType.NUMBER) {
            return new NumberValue(val + ((NumberValue) other).val);
        }

        return null;
    }

    @Override
    public Value subtract(Value other) throws RuntimeError {
        if(other.getType() == ValueType.NUMBER) {
            return new NumberValue(val + (double) other.unwrap());
        }
        throw new RuntimeError("Unsupported operation");
    }

    @Override
    public Value multiply(Value other) throws RuntimeError {
        if(other.getType() == ValueType.NUMBER) {
            return new NumberValue(val * (double) other.unwrap());
        }
        throw new RuntimeError("Unsupported operation");
    }

    @Override
    public Value divide(Value other) throws RuntimeError {
        if(other.getType() == ValueType.NUMBER) {
            return new NumberValue(val / (double) other.unwrap());
        }
        throw new RuntimeError("Unsupported operation");
    }
}
