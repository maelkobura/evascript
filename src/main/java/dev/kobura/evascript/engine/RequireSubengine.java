package dev.kobura.evascript.engine;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.registerable.Register;
import dev.kobura.evascript.runtime.value.Value;

import java.util.List;

public interface RequireSubengine {

    public List<Register> getRegisters();

    public boolean isEnableRegister();

    public void register(Register register);

}
