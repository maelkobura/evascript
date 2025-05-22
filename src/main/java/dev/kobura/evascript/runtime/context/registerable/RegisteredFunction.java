package dev.kobura.evascript.runtime.context.registerable;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.value.Value;

public interface RegisteredFunction {

    String[] requiredPermissions();

    String name();

    public Value invoke(Execution execution, Value...values) throws RuntimeError;

}
