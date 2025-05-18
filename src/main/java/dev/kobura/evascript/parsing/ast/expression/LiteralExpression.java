package dev.kobura.evascript.parsing.ast.expression;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;
import lombok.Getter;

@Getter
public class LiteralExpression extends ASTExpression {
    Object value;

    public LiteralExpression(Object value, Token token) {
        this.value = value;
        this.line = token.line;
    }

    @Override
    public String toString() {
        return "LiteralExpression(value=" + value + ")";
    }

    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value...values) throws RuntimeError {
        return visitor.visitLiteralExpression(this, execution);
    }
}
