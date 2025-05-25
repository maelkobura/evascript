package dev.kobura.evascript.globals;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.Scriptable;

@Scriptable(defaultName = "math")
public class Math {

    @Scriptable
    public double floor(double value) {
        return java.lang.Math.floor(value);
    }

    @Scriptable
    public double ceil(double value) {
        return java.lang.Math.ceil(value);
    }

    @Scriptable
    public long round(double value) {
        return java.lang.Math.round(value);
    }

    @Scriptable
    public double sqrt(double value) throws RuntimeError {
        if (value < 0) {
            throw new RuntimeError("Cannot compute square root of a negative number");
        }
        return java.lang.Math.sqrt(value);
    }

    @Scriptable
    public double pow(double base, double exponent) {
        return java.lang.Math.pow(base, exponent);
    }
}
