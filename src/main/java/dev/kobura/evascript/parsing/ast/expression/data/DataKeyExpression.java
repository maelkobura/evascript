package dev.kobura.evascript.parsing.ast.expression.data;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.UndefinedValue;
import dev.kobura.evascript.runtime.value.Value;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class DataKeyExpression extends ASTExpression {

    Token key;

    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value... values) throws RuntimeError {
        return visitor.visitDataKeyExpression(this, execution);
    }
}
