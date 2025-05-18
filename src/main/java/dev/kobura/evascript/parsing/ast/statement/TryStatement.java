package dev.kobura.evascript.parsing.ast.statement;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.parsing.ast.ASTStatement;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class TryStatement extends ASTStatement {

    BlockStatement tryBlock;
    BlockStatement catchBlock;
    String catchVariable;
    BlockStatement finallyBlock;

    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value... values) throws RuntimeError {
        return visitor.visitTryStatement(this, execution);
    }
}
