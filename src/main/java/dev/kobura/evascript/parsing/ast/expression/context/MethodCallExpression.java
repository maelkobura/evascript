package dev.kobura.evascript.parsing.ast.expression.context;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.parsing.ast.expression.context.variable.args.ArgumentExpression;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class MethodCallExpression extends ASTExpression {

    ASTExpression target; // Ex: obj dans obj.method()
    String methodName;
    ArgumentExpression arguments;

    public MethodCallExpression(ASTExpression target, String methodName, ArgumentExpression arguments, Token token) {
        this.target = target;
        this.methodName = methodName;
        this.arguments = arguments;
        this.line = token.line;
    }

    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value...values) throws RuntimeError {
        return visitor.visitMethodCallExpression(this, execution);
    }
}
