package dev.kobura.evascript.parsing.ast.expression;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.parsing.ast.expression.context.variable.args.ArgumentDeclarationExpression;
import dev.kobura.evascript.parsing.ast.statement.BlockStatement;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class LambdaExpression extends ASTExpression {

    ArgumentDeclarationExpression expression;
    BlockStatement body;
    boolean async;

    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value... values) throws RuntimeError {
        return visitor.visitLambdaExpression(this, execution);
    }
}
