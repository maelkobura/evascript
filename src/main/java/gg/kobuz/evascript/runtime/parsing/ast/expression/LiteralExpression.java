package gg.kobuz.evascript.runtime.parsing.ast.expression;

import gg.kobuz.evascript.runtime.parsing.ast.ASTExpression;

public class LiteralExpression extends ASTExpression {
    Object value;

    public LiteralExpression(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LiteralExpression(value=" + value + ")";
    }
}
