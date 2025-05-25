package dev.kobura.evascript.engine.register;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.registerable.RegisteredFunction;
import dev.kobura.evascript.runtime.value.ArrayValue;
import dev.kobura.evascript.runtime.value.Value;

import java.util.ArrayList;
import java.util.List;

public class RangeFunction implements RegisteredFunction {
    @Override
    public String[] requiredPermissions() {
        return new String[0];
    }

    @Override
    public String name() {
        return "range";
    }

    @Override
    public Value invoke(Execution execution, Value... values) throws RuntimeError {
        int start = 0;
        int end;
        int step = 1;

        if (values.length == 0) {
            throw new RuntimeError("@range requires at least one argument");
        }

        // Lecture des arguments dans l'ordre : start, end, step
        if (values.length == 1) {
            end = (int) values[0].unwrap();
        } else {
            start = (int) values[0].unwrap();
            end = (int) values[1].unwrap();
            if (values.length >= 3) {
                step = (int) values[2].unwrap();
            }
        }

        if (step == 0) {
            throw new RuntimeError("step cannot be 0 in @range");
        }

        List<Value> vs = new ArrayList<>();

        if (step > 0) {
            for (int i = start; i < end; i += step) {
                vs.add(Value.from(i));
            }
        } else {
            for (int i = start; i > end; i += step) {
                vs.add(Value.from(i));
            }
        }

        return new ArrayValue(vs);
    }
}
