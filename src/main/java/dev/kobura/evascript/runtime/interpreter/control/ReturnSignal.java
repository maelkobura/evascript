package dev.kobura.evascript.runtime.interpreter.control;

import dev.kobura.evascript.runtime.value.Value;

public class ReturnSignal extends RuntimeException {
    private final Value returnValue;

    public ReturnSignal(Value returnValue) {
        this.returnValue = returnValue;
    }

    public Value getReturnValue() {
        return returnValue;
    }

}
