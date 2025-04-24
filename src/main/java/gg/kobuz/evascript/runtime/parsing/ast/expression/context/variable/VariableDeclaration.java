package gg.kobuz.evascript.runtime.parsing.ast.expression.context.variable;

import gg.kobuz.evascript.lexer.token.TokenType;
import gg.kobuz.evascript.runtime.parsing.ast.ASTExpression;
import gg.kobuz.evascript.runtime.parsing.ast.ASTStatement;

public class VariableDeclaration extends ASTStatement {
    public String name;
    ASTExpression value;
    TokenType mode;

    public VariableDeclaration(String name, ASTExpression value, TokenType mode) {
        this.name = name;
        this.value = value;
        this.mode = mode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VariableDeclaration{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value=").append(value);
        sb.append(", mode=").append(mode);
        sb.append('}');
        return sb.toString();
    }
}
