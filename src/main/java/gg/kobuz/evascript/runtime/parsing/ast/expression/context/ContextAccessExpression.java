package gg.kobuz.evascript.runtime.parsing.ast.expression.context;

import gg.kobuz.evascript.runtime.parsing.ast.ASTExpression;

public class ContextAccessExpression extends ASTExpression{

    ASTExpression target; // Ex: obj dans obj.x
    public String identifier;

    public ContextAccessExpression(ASTExpression target, String identifier) {
        this.target = target;
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "ContextAccessExpression{" +
                "target=" + target +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
