package gg.kobuz.evascript.runtime.parsing.ast.expression.context.variable.args;

import gg.kobuz.evascript.runtime.parsing.ast.ASTExpression;

import java.util.Map;

public class StructuredArgumentExpression extends ArgumentExpression {

    Map<String, ASTExpression> arguments;

    public StructuredArgumentExpression(Map<String, ASTExpression> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StructuredArguementExpression{");
        sb.append("arguments=").append(arguments);
        sb.append('}');
        return sb.toString();
    }
}
