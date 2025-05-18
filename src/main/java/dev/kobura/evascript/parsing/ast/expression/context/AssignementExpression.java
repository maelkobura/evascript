package dev.kobura.evascript.parsing.ast.expression.context;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class AssignementExpression extends ASTExpression {
    ASTExpression target;
    ASTExpression value;


    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value... values) throws RuntimeError {
        return visitor.visitAssignementExpression(this, execution);
    }
}
