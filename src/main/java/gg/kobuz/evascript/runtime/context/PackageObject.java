package gg.kobuz.evascript.runtime.context;

import gg.kobuz.evascript.errors.RuntimeError;
import gg.kobuz.evascript.runtime.parsing.ast.expression.context.variable.args.ArgumentExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageObject implements ContextObject{

    private ContextObject parent;
    private Map<String, ContextObject> children;
    private String name;

    @Override
    public void init(ContextObject parent) {
        this.parent = parent;
        this.children = new HashMap<>();
    }

    @Override
    public Object execute(ArgumentExpression expression) throws RuntimeError {
        return null;
    }

    @Override
    public ContextObject child(String identifier) throws RuntimeError {
        return null;
    }

    public PackageObject addChild(String name, ContextObject obj) {
        obj.init(this);
        if(obj instanceof PackageObject) {
            ((PackageObject) obj).name = name;
        }
        children.put(name, obj);
        return this;
    }

    @Override
    public Object value() {
        return name + " (" + children.size() + " child)";
    }

    @Override
    public ContextObject parent() {
        return parent;
    }

    @Override
    public String type() {
        return "package";
    }
}
