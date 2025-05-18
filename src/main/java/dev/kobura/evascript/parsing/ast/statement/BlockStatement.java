package dev.kobura.evascript.parsing.ast.statement;


import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.parsing.ast.ASTStatement;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.value.Value;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BlockStatement extends ASTStatement {

    List<ASTStatement> statements;

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


    @Override
    public Value accept(NodeVisitor visitor, Execution execution, Value...values) throws RuntimeError {
        return visitor.visitBlockStatement(this, execution);
    }
}
