package dev.kobura.evascript.parsing.ast.statement;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.parsing.ast.ASTStatement;
import dev.kobura.evascript.parsing.ast.expression.ForeachExpression;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;
import lombok.Getter;

@Getter
public class ForStatement extends ASTStatement {
    ForeachExpression expression;
    ASTStatement body;

    public ForStatement(ForeachExpression expression, ASTStatement body) {
        this.expression = expression;
        this.body = body;
    }

    @Override
    public String toString() {
        return "ForStatement{" +
                "expression=" + expression +
                ", body=" + body +
                '}';
    }

    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value...values) throws RuntimeError {
        return visitor.visitForStatement(this, execution);
    }
}
