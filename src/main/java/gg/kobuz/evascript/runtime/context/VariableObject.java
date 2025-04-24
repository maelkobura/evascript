package gg.kobuz.evascript.runtime.context;

import gg.kobuz.evascript.errors.RuntimeError;
import gg.kobuz.evascript.runtime.parsing.ast.expression.context.variable.args.ArgumentExpression;
import gg.kobuz.evascript.runtime.parsing.ast.statement.BlockStatement;

public class VariableObject implements ContextObject {

    private Object value;
    private ContextObject parent;

    @Override
    public void init(ContextObject parent) {
        this.parent = parent;
    }

    @Override
    public Object execute(ArgumentExpression expression) throws RuntimeError {
        if(value instanceof BlockStatement) {
            //TODO execution
        }
        throw new RuntimeError("Not a function");
    }

    @Override
    public ContextObject child(String identifier) throws RuntimeError {
        throw new RuntimeError("Not a package");
    }

    @Override
    public Object value() {
        if(value instanceof ContextObject) {
            return ((ContextObject) value).value();
        }
        return value;
    }

    @Override
    public ContextObject parent() {
        return parent;
    }

    @Override
    public String type() {
        return value.getClass().getTypeName();
    }
}
