package dev.kobura.evascript.runtime.context;

import dev.kobura.evascript.ScriptEngine;
import dev.kobura.evascript.runtime.value.UndefinedValue;
import dev.kobura.evascript.runtime.value.Value;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Scope {

    private final ConcurrentMap<ContextIdentity, Value> varPool;
    private final ScriptEngine engine;

    private Scope parent;

    public Value get(String name) {
        for (ContextIdentity id : varPool.keySet()) {
            if (id.getName().equals(name)) {
                return varPool.get(id);
            }
        }
        if(parent != null) {
            return parent.get(name);
        }
        return UndefinedValue.INSTANCE;
    }

    public Scope(ScriptEngine engine) {
        this.engine = engine;
        this.varPool = new ConcurrentHashMap<>();
    }

    public Scope(Scope parent) {
        this.parent = parent;
        this.varPool = new ConcurrentHashMap<>();
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

    public void refresh() {
        for (ContextIdentity id : varPool.keySet()) {
            if(id.getExpiration() != 0) {
                Duration duration = Duration.between(id.getCreation(), Instant.now());
                if(duration.toMillis() >= id.getExpiration()) {
                    undefine(id.getName());
                }
            }
        }
    }

}
