package gg.kobuz.evascript.runtime.parsing.ast.expression.logical;

import gg.kobuz.evascript.lexer.token.LogicToken;
import gg.kobuz.evascript.runtime.parsing.ast.ASTExpression;

public class ComparisonExpression extends ASTExpression {

    public ASTExpression left;
    public LogicToken operator;
    public ASTExpression right;

    public ComparisonExpression(ASTExpression left, LogicToken operator, ASTExpression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return "ComparisonExpression{" +
                "left=" + left +
                ", operator=" + operator +
                ", right=" + right +
                '}';
    }
}
