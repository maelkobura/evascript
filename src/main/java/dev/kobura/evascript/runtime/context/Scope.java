package dev.kobura.evascript.runtime.context;

import dev.kobura.evascript.ScriptEngine;
import dev.kobura.evascript.runtime.value.Value;

import java.util.HashMap;
import java.util.Map;

public class Scope {

    private Map<ContextIdentity, Value> varPool;
    private ScriptEngine engine;

    private Scope parent;

    public Value get(String name) {
        return varPool.keySet().stream()
                .filter(identity -> identity.getName().equals(name))
                .findFirst()
                .map(varPool::get)
                .orElse(parent != null ? parent.get(name) : engine.getBuildInByName(name));
    }

    public Scope(ScriptEngine engine) {
        this.engine = engine;
        this.varPool = new HashMap<>();
    }

    public Scope(Scope parent) {
        this.parent = parent;
        this.varPool = new HashMap<>();
        this.engine = parent.engine;
    }

    public Scope getMain() {
        return parent != null ? parent.getMain() : this;
    }

    public void undefine(String name) {
        ContextIdentity id = varPool.keySet().stream().filter(identity -> identity.getName().equals(name)).findFirst().orElse(null);
        if(id == null && parent != null) {
            parent.undefine(name);
            return;
        }
        varPool.remove(id);
    }

    public void set(String name, Value value) {
        ContextIdentity id = varPool.keySet().stream().filter(identity -> identity.getName().equals(name)).findFirst().orElse(null);
        if(id == null && parent != null) {
            parent.set(name, value);
            return;
        }
        varPool.put(id, value);
    }

    public void set(ContextIdentity identity, Value value) {
        varPool.put(identity, value);
    }

}
