package dev.kobura.evascript.parsing.ast.expression.tokenable;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.NullValue;
import dev.kobura.evascript.runtime.value.Value;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class NullExpression extends ASTExpression {

    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value... values) throws RuntimeError {
        return NullValue.INSTANCE;
    }
}
