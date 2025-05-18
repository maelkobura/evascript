package dev.kobura.evascript.parsing.ast.expression.logical;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.lexer.token.ArithmeticToken;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class BinaryExpression extends ASTExpression {

    ASTExpression left;
    ArithmeticToken operator;
    ASTExpression right;

    public BinaryExpression(ASTExpression left, Token operator, ASTExpression right) {
        this.left = left;
        this.operator = (ArithmeticToken) operator.type;
        this.right = right;
        this.line = operator.line;
    }


    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value...values) throws RuntimeError {
        return visitor.visitBinaryExpression(this, execution);
    }
}
