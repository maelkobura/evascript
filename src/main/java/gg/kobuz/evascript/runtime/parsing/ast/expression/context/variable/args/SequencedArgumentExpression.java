package gg.kobuz.evascript.runtime.parsing.ast.expression.context.variable.args;

import gg.kobuz.evascript.runtime.parsing.ast.ASTExpression;

import java.util.List;

public class SequencedArgumentExpression extends ArgumentExpression {

    private List<ASTExpression> arguments;

    public SequencedArgumentExpression(List<ASTExpression> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SequencedArgumentExpression{");
        sb.append("arguments=").append(arguments);
        sb.append('}');
        return sb.toString();
    }
}
