package gg.kobuz.evascript.runtime.parsing.ast.statement;

import gg.kobuz.evascript.runtime.parsing.ast.ASTExpression;
import gg.kobuz.evascript.runtime.parsing.ast.ASTStatement;

public class IfStatement extends ASTStatement {
    public ASTStatement elseBody;
    ASTExpression condition;
    public ASTStatement body;

    public IfStatement(ASTExpression condition, ASTStatement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toString() {
        return "IfStatement{" +
                "elseBody=" + elseBody +
                ", condition=" + condition +
                ", body=" + body +
                '}';
    }
}
