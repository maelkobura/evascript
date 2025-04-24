package gg.kobuz.evascript.runtime.parsing.ast.statement;

import gg.kobuz.evascript.runtime.parsing.ast.ASTExpression;
import gg.kobuz.evascript.runtime.parsing.ast.ASTStatement;

public class WhileStatement extends ASTStatement {
    ASTExpression condition;
    public ASTStatement body;

    public WhileStatement(ASTExpression condition, ASTStatement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WhileStatement{");
        sb.append("condition=").append(condition);
        sb.append(", body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}
