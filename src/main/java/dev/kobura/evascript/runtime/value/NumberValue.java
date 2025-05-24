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
    public String toString() {
        return String.valueOf(unwrap());
    }

    @Override
    public Value add(Value other) throws RuntimeError {

        try {
        if(other.getType() == ValueType.STRING) {
            return new StringValue(val + other.toString());
        }else if(other.getType() == ValueType.NUMBER) {
            return new NumberValue(val + ((NumberValue) other).val);
        }
        } catch (Exception e) {
            throw new RuntimeError(e.getMessage());
        }

        return null;
    }

    @Override
    public Value subtract(Value other) throws RuntimeError {
        if(other.getType() == ValueType.NUMBER) {
            try {
                return new NumberValue(val - ((NumberValue) other).val);
            } catch (Exception e) {
                throw new RuntimeError(e.getMessage());
            }
        }
        throw new RuntimeError("Unsupported operation");
    }

    @Override
    public Value multiply(Value other) throws RuntimeError {
        if(other.getType() == ValueType.NUMBER) {
            try {
                return new NumberValue(val * ((NumberValue) other).val);
            } catch (Exception e) {
                throw new RuntimeError(e.getMessage());
            }
        }
        throw new RuntimeError("Unsupported operation");
    }

    @Override
    public Value divide(Value other) throws RuntimeError {
        if(other.getType() == ValueType.NUMBER) {
            try {
                if(((NumberValue) other).val == 0) {
                    throw new RuntimeError("Division by zero");
                }
                return new NumberValue(val / ((NumberValue) other).val);
            } catch (Exception e) {
                throw new RuntimeError(e.getMessage());
            }
        }
        throw new RuntimeError("Unsupported operation");
    }
}
