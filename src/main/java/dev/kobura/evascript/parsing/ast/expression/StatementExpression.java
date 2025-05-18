package dev.kobura.evascript.parsing.ast.expression;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.parsing.ast.ASTStatement;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.UndefinedValue;
import dev.kobura.evascript.runtime.value.Value;

public class StatementExpression extends ASTExpression {

    ASTStatement statement;

    public StatementExpression(ASTStatement statement) {
        this.statement = statement;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatementExpression{");
        sb.append("statement=").append(statement);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value... values) throws RuntimeError {
        return UndefinedValue.INSTANCE;
    }
}
