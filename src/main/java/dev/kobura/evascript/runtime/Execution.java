package dev.kobura.evascript.runtime;

import dev.kobura.evascript.engine.ScriptEngine;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.parsing.ast.statement.BlockStatement;
import dev.kobura.evascript.parsing.ast.statement.ExpressionStatement;
import dev.kobura.evascript.runtime.context.ContextIdentity;
import dev.kobura.evascript.runtime.context.Scope;
import dev.kobura.evascript.runtime.interpreter.Interpreter;
import dev.kobura.evascript.runtime.interpreter.NodeVisitor;
import dev.kobura.evascript.runtime.interpreter.control.BreakSignal;
import dev.kobura.evascript.runtime.interpreter.control.ContinueSignal;
import dev.kobura.evascript.runtime.interpreter.control.ReturnSignal;
import dev.kobura.evascript.runtime.value.NullValue;
import dev.kobura.evascript.runtime.value.UndefinedValue;
import dev.kobura.evascript.runtime.value.Value;
import dev.kobura.evascript.security.PermissiveUser;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Execution {

    @Getter
    private final ScriptEngine engine;
    @Getter
    private final Scope scope;
    private Execution parent;
    @Getter
    private final PermissiveUser user;
    @Getter
    private Map<String, Object> contextData;

    public Execution(ScriptEngine engine, Execution parent, PermissiveUser user) {
        this.engine = engine;
        this.parent = parent;
        this.scope = new Scope(parent.scope);
        this.user = user;
        this.contextData = parent.contextData;

        this.contextData.put("execution", this);
        this.contextData.put("user", user);

    }

    public Execution(ScriptEngine engine, Scope scope, PermissiveUser user, Map<String, Object> contextData) {
        this.engine = engine;
        this.scope = scope;
        this.contextData = new HashMap<>(contextData);
        this.user = user;

        this.contextData.put("execution", this);
        this.contextData.put("user", user);
    }

    public void var(ContextIdentity identity, Value value) throws RuntimeError {
        if(scope.get(identity.getName()) != UndefinedValue.INSTANCE) {
            throw new RuntimeError(identity.getName() +" already defined in current scope");
        }
        scope.getMain().set(identity, value);
    }

    public void let(ContextIdentity identity, Value value) throws RuntimeError {
        if(scope.get(identity.getName()) != UndefinedValue.INSTANCE) {
            throw new RuntimeError(identity.getName() +" already defined in current scope");
        }
        scope.set(identity, value);
    }

    public void redefine(ContextIdentity older, ContextIdentity newer) {
        scope.redefine(older, newer);
    }

    public void set(String name, Value value) throws RuntimeError {
        scope.set(name, value);
    }

    public void undefine(String name) {
        scope.undefine(name);
    }

    public Value get(String name) {
        return scope.get(name);
    }

    public Value get(ContextIdentity identity) {
        return scope.get(identity);
    }

    public Value runAsFunction(BlockStatement block) throws RuntimeError {
        NodeVisitor visitor = new Interpreter();
        try {
            block.accept(visitor, this);
        }catch (ReturnSignal e) {
            return e.getReturnValue();
        }catch (BreakSignal e) {
            throw new RuntimeError("Unsupported break statement in root code");
        }catch (ContinueSignal e) {
            throw new RuntimeError("Unsupported continue statement in root code");
        }
        return NullValue.INSTANCE;
    }

    public void runRoot(BlockStatement block) throws RuntimeError {
        NodeVisitor visitor = new Interpreter();
        try {
            block.accept(visitor, this);
        }catch (ReturnSignal e) {
            throw new RuntimeError("Unsupported return statement in root code");
        }catch (BreakSignal e) {
            throw new RuntimeError("Unsupported break statement in root code");
        }catch (ContinueSignal e) {
            throw new RuntimeError("Unsupported continue statement in root code");
        }
    }

    public Value runEmbed(ExpressionStatement stmt) throws RuntimeError {
        NodeVisitor visitor = new Interpreter();
        try {
            return stmt.accept(visitor, this);
        }catch (ReturnSignal e) {
            throw new RuntimeError("Unsupported return statement in embed code");
        }catch (BreakSignal e) {
            throw new RuntimeError("Unsupported break statement in embed code");
        }catch (ContinueSignal e) {
            throw new RuntimeError("Unsupported continue statement in embed code");
        }
    }


    public Value run(BlockStatement block) throws RuntimeError {
        NodeVisitor visitor = new Interpreter();
        try {
            return block.accept(visitor, this);
        }catch (ReturnSignal e) {
            throw new RuntimeError("Unsupported return statement in root code");
        }catch (BreakSignal e) {
            throw new RuntimeError("Unsupported break statement in root code");
        }catch (ContinueSignal e) {
            throw new RuntimeError("Unsupported continue statement in root code");
        }
    }
}
