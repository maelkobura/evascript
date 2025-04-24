package gg.kobuz.evascript.runtime.parsing.ast.statement;

import gg.kobuz.evascript.runtime.parsing.ast.ASTStatement;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement extends ASTStatement {

    public List<ASTStatement> statements;

    public BlockStatement() {
        this.statements = new ArrayList<>();
    }

    public void addStatement(ASTStatement statement) {
        statements.add(statement);
    }

    @Override
    public String toString() {
        return "BlockStatement(statements=" + statements + ")";
    }

}
