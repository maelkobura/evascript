package dev.kobura.evascript.parsing.ast.expression.logical;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.lexer.token.LogicToken;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ComparisonExpression extends ASTExpression {

    public ASTExpression left;
    public LogicToken operator;
    public ASTExpression right;

    public ComparisonExpression(ASTExpression left, Token operator, ASTExpression right) {
        this.left = left;
        this.operator = (LogicToken) operator.type;
        this.right = right;
        this.line = operator.line;
    }

    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value...values) throws RuntimeError {
        return visitor.visitComparisonExpression(this, execution);
    }
}
