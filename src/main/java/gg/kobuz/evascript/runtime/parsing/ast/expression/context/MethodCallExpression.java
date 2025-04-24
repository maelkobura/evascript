package gg.kobuz.evascript.runtime.parsing.ast.expression.context;

import gg.kobuz.evascript.runtime.parsing.ast.ASTExpression;
import gg.kobuz.evascript.runtime.parsing.ast.expression.context.variable.args.ArgumentExpression;

public class MethodCallExpression extends ASTExpression{

    ASTExpression target; // Ex: obj dans obj.method()
    String methodName;
    ArgumentExpression arguments;

    public MethodCallExpression(ASTExpression target, String methodName, ArgumentExpression arguments) {
        this.target = target;
        this.methodName = methodName;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "MethodCallExpression{" +
                "target=" + target +
                ", methodName='" + methodName + '\'' +
                ", arguments=" + arguments +
                '}';
    }
}
