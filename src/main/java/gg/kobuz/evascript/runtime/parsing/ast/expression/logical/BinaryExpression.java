package gg.kobuz.evascript.runtime.parsing.ast.expression.logical;

import gg.kobuz.evascript.lexer.token.ArithmeticToken;
import gg.kobuz.evascript.runtime.parsing.ast.ASTExpression;
import lombok.ToString;

@ToString
public class BinaryExpression extends ASTExpression {

    ASTExpression left;
    ArithmeticToken operator;
    ASTExpression right;

    public BinaryExpression(ASTExpression left, ArithmeticToken operator, ASTExpression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }


}
