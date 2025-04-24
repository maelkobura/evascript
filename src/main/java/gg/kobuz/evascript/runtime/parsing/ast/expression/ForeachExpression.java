package gg.kobuz.evascript.runtime.parsing.ast.expression;

import gg.kobuz.evascript.runtime.parsing.ast.ASTExpression;

public class ForeachExpression extends ASTExpression {

    String varName;
    ASTExpression iterable;

    public ForeachExpression(String varName, ASTExpression iterable) {
        this.varName = varName;
        this.iterable = iterable;
    }

    @Override
    public String toString() {
        return "ForeachExpression{" +
                "varName='" + varName + '\'' +
                ", iterable=" + iterable +
                '}';
    }
}
