package dev.kobura.evascript.parsing.ast.statement;


import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.parsing.ast.ASTStatement;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;
import lombok.Getter;

@Getter
public class WhileStatement extends ASTStatement {
    public ASTStatement body;
    ASTExpression condition;

    public WhileStatement(ASTExpression condition, ASTStatement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WhileStatement{");
        sb.append("condition=").append(condition);
        sb.append(", body=").append(body);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value... values) throws RuntimeError {
        return visitor.visitWhileStatement(this, execution);
    }
}
