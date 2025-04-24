package gg.kobuz.evascript.runtime.parsing.ast.expression;

import gg.kobuz.evascript.runtime.parsing.ast.ASTExpression;
import gg.kobuz.evascript.runtime.parsing.ast.ASTStatement;

public class StatementExpression extends ASTExpression {

    ASTStatement statement;

    public StatementExpression(ASTStatement statement) {
        this.statement = statement;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatementExpression{");
        sb.append("statement=").append(statement);
        sb.append('}');
        return sb.toString();
    }
}
