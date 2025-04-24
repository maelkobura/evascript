package gg.kobuz.evascript.runtime.parsing.ast.statement;

import gg.kobuz.evascript.runtime.parsing.ast.ASTExpression;
import gg.kobuz.evascript.runtime.parsing.ast.ASTStatement;

public class ExpressionStatement extends ASTStatement {
    public ASTExpression expression;

    public ExpressionStatement(ASTExpression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "ExpressionStatement{" +
                "expression=" + expression +
                '}';
    }
}
