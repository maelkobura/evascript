package dev.kobura.evascript.parsing.ast.statement.loop;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.parsing.ast.ASTStatement;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.interpreter.control.BreakSignal;
import dev.kobura.evascript.runtime.value.UndefinedValue;
import dev.kobura.evascript.runtime.value.Value;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class BreakStatement extends ASTStatement {
    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value... values) throws RuntimeError {
        throw new BreakSignal();
    }
}
