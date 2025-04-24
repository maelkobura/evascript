package gg.kobuz.evascript.runtime.context;

import gg.kobuz.evascript.errors.RuntimeError;
import gg.kobuz.evascript.runtime.parsing.ast.expression.context.variable.args.ArgumentExpression;
import gg.kobuz.evascript.runtime.parsing.ast.expression.context.variable.args.StructuredArgumentExpression;

public interface ContextObject {

    public void init(ContextObject parent);
    public Object execute(ArgumentExpression expression) throws RuntimeError;
    public ContextObject child(String identifier) throws RuntimeError;
    public Object value();
    public ContextObject parent();
    public String type();


}
