package dev.kobura.evascript.parsing.ast.expression.context.variable;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.lexer.token.SyntaxToken;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.parsing.ast.ASTStatement;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class VariableDeclaration extends ASTStatement {
    public String name;
    ASTExpression value;
    SyntaxToken mode;
    public int line;

    public VariableDeclaration(String name, ASTExpression value, SyntaxToken mode, Token token) {
        this.name = name;
        this.value = value;
        this.mode = mode;
        this.line = token.line;
    }

    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value...values) throws RuntimeError {
        return visitor.visitVariableDeclaration(this, execution);
    }
}
