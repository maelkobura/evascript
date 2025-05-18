package dev.kobura.evascript.parsing.ast.statement;


import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.parsing.ast.ASTStatement;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;
import lombok.Getter;

@Getter
public class ExpressionStatement extends ASTStatement {
    ASTExpression expression;

    public ExpressionStatement(ASTExpression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "ExpressionStatement{" +
                "expression=" + expression +
                '}';
    }


    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value... values) throws RuntimeError {
        return visitor.visitExpressionStatement(this, execution);
    }
}
