package dev.kobura.evascript.parsing.ast.expression.context.variable.args;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@AllArgsConstructor
@ToString
public class StructuredArgumentExpression extends ArgumentExpression {

    Map<String, ASTExpression> arguments;

    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value...values) throws RuntimeError {
        return visitor.visitStructuredArgumentExpression(this, execution);
    }
}
