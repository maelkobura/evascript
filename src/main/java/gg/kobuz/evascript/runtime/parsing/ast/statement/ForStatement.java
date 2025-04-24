package gg.kobuz.evascript.runtime.parsing.ast.statement;

import gg.kobuz.evascript.runtime.parsing.ast.ASTStatement;
import gg.kobuz.evascript.runtime.parsing.ast.expression.ForeachExpression;

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
}
