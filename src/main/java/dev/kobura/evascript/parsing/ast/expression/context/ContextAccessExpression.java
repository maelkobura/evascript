package dev.kobura.evascript.parsing.ast.expression.context;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;
import lombok.Getter;

@Getter
public class ContextAccessExpression extends ASTExpression {

    String identifier;
    ASTExpression target; // Ex: obj dans obj.x

    public ContextAccessExpression(ASTExpression target, String identifier, Token token) {
        this.target = target;
        this.identifier = identifier;
        this.line = token.line;
    }

    @Override
    public String toString() {
        return "ContextAccessExpression{" +
                "target=" + target +
                ", identifier='" + identifier + '\'' +
                '}';
    }

    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value... values) throws RuntimeError {
        return visitor.visitContextAccessExpression(this, execution);
    }
}
