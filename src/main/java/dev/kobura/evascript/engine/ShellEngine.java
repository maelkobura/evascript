package dev.kobura.evascript.engine;

import dev.kobura.evascript.ScriptEngine;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.lexer.EvaLexer;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.EvaParser;
import dev.kobura.evascript.parsing.ast.statement.BlockStatement;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.ContextIdentity;
import dev.kobura.evascript.runtime.context.Scope;
import dev.kobura.evascript.runtime.context.Scriptable;
import dev.kobura.evascript.runtime.context.registerable.Register;
import dev.kobura.evascript.runtime.context.registerable.RegisteredFunction;
import dev.kobura.evascript.runtime.value.UndefinedValue;
import dev.kobura.evascript.runtime.value.Value;
import dev.kobura.evascript.security.PermissiveUser;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShellEngine implements ScriptEngine {


    protected long defaultExpirationTime;
    protected long defaultConstExpirationTime;
    protected boolean disableExpiration;
    protected boolean allowConstant;
    protected boolean verbose;
    protected boolean enableGlobals;
    protected boolean enableRegister;
    protected boolean enableLoad;
    protected String builtinPackage;
    protected Class<? extends PermissiveUser> securityAgent;

    public ShellEngine(
            long defaultExpirationTime,
            long defaultConstExpirationTime,
            boolean disableExpiration,
            boolean allowConstant,
            boolean verbose,
            boolean enableGlobals,
            boolean enableRegister,
            boolean enableLoad,
            String builtinPackage,
            Class<? extends PermissiveUser> securityAgent
    ) throws LoadingBuildinException {
        this.defaultExpirationTime = defaultExpirationTime;
        this.defaultConstExpirationTime = defaultConstExpirationTime;
        this.disableExpiration = disableExpiration;
        this.allowConstant = allowConstant;
        this.verbose = verbose;
        this.enableGlobals = enableGlobals;
        this.enableRegister = enableRegister;
        this.enableLoad = enableLoad;
        this.builtinPackage = builtinPackage;
        this.securityAgent = securityAgent;

        try (ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages(builtinPackage)
                .scan()) {

            for(ClassInfo classInfo : scanResult.getClassesWithAnnotation(Scriptable.class.getName())) {
                if (!classInfo.getOuterClasses().isEmpty()) {
                    continue;
                }
                try {
                    String className = classInfo.getName();
                    Class<?> clazz = Class.forName(className);
                    if (!clazz.isAnnotationPresent(Scriptable.class))
                        throw new LoadingBuildinException(className + ": the class hasn't Scriptable");
                    Scriptable scriptable = clazz.getAnnotation(Scriptable.class);
                    if (scriptable.defaultName().isEmpty()) {
                        throw new LoadingBuildinException(className + ": the class hasn't default name");
                    }
                    Object obj = clazz.getDeclaredConstructor().newInstance();
                    ContextIdentity identity = new ContextIdentity(scriptable.defaultName(), true, Instant.now(), 0);
                    builtin.put(identity, Value.from(obj));
                }catch(Exception e){
                    throw new LoadingBuildinException(e);
                }
            }

        }

    }

    private final Map<ContextIdentity, Value> builtin = new HashMap<>();
    private final List<Register> registers = new ArrayList<Register>();
    private final List<RegisteredFunction> systems = new ArrayList<>();

    @Override
    public void register(Register register) {
        registers.add(register);
    }

    @Override
    public String run(String code, Scope scope, PermissiveUser user) throws RuntimeError {
        scope.refresh();

        if(!code.endsWith(";")) {
            code += ";";
        }

        EvaLexer lexer = new EvaLexer(code);
        List<Token> tokens = lexer.readAll();

        EvaParser parser = new EvaParser(tokens);
        BlockStatement block = parser.parseAsBlock();

        Execution execution = new Execution(this, scope, user, new HashMap<>());
        return execution.run(block).toString();
    }

    @Override
    public String run(File file, Scope scope, PermissiveUser user) throws RuntimeError, IOException {
        FileInputStream in = new FileInputStream(file);
        return run(new String(in.readAllBytes()), scope, user);
    }

    @Override
    public void systemMethod(RegisteredFunction function) {
        systems.add(function);
    }

    @Override
    public boolean checkPermissions(PermissiveUser user, String...permissions) {
        if(securityAgent == null) return true;
        if(user == null || !(user.getClass().isAssignableFrom(securityAgent))) return false;
        for(String permission : permissions) {
            if(user.hasPermission(permission)) return true;
        }
        return false;
    }

    @Override
    public Scope createScope() {
        Scope scope = new Scope(this);
        for(Map.Entry<ContextIdentity, Value> entry : builtin.entrySet()) {
            scope.set(entry.getKey(), entry.getValue());
        }
        return scope;
    }


    @Override
    public long getDefaultExpirationTime() {
        return defaultExpirationTime;
    }

    @Override
    public long getDefaultConstExpirationTime() {
        return defaultConstExpirationTime;
    }

    @Override
    public boolean isDisableExpiration() {
        return disableExpiration;
    }

    @Override
    public boolean isAllowConstant() {
        return allowConstant;
    }

    @Override
    public boolean isVerbose() {
        return verbose;
    }

    @Override
    public boolean isEnableGlobals() {
        return enableGlobals;
    }

    @Override
    public boolean isEnableRegister() {
        return enableRegister;
    }

    @Override
    public boolean isEnableLoad() {
        return enableLoad;
    }

    @Override
    public String getBuiltinPackage() {
        return builtinPackage;
    }

    @Override
    public Value invokeSystem(Execution execution, String name, Value... values) throws RuntimeError {
        for(RegisteredFunction function : systems) {
            if(function.name().equals(name) && checkPermissions(execution.getUser(), function.requiredPermissions())) {
                Value ret = function.invoke(execution, values);
                return ret == null ? UndefinedValue.INSTANCE : ret;
            }
        }
        return UndefinedValue.INSTANCE;
    }

    @Override
    public Class<? extends PermissiveUser> getSecurityAgent() {
        return securityAgent;
    }

    @Override
    public Value getBuiltin(String name) {
        return null;
    }

    @Override
    public Map<ContextIdentity, Value> getAllBuiltin() {
        return builtin;
    }

    public static class ShellEngineBuilder {
        private long defaultExpirationTime = 600000;
        private long defaultConstExpirationTime = 3600000;
        private boolean disableExpiration = false;
        private boolean allowConstant = true;
        private boolean verbose = false;
        private boolean enableGlobals = true;
        private boolean enableRegister = true;
        private boolean enableLoad = true;
        private String builtinPackage = "dev.kobura.evascript.globals";
        private Class<? extends PermissiveUser> securityAgent = null;

        protected ShellEngineBuilder() {}

        public ShellEngine.ShellEngineBuilder defaultExpirationTime(long defaultExpirationTime) {
            this.defaultExpirationTime = defaultExpirationTime;
            return this;
        }

        public ShellEngine.ShellEngineBuilder defaultConstExpirationTime(long defaultConstExpirationTime) {
            this.defaultConstExpirationTime = defaultConstExpirationTime;
            return this;
        }

        public ShellEngine.ShellEngineBuilder disableExpiration(boolean disableExpiration) {
            this.disableExpiration = disableExpiration;
            return this;
        }

        public ShellEngine.ShellEngineBuilder allowConstant(boolean allowConstant) {
            this.allowConstant = allowConstant;
            return this;
        }

        public ShellEngine.ShellEngineBuilder verbose(boolean verbose) {
            this.verbose = verbose;
            return this;
        }

        public ShellEngine.ShellEngineBuilder enableGlobals(boolean enableGlobals) {
            this.enableGlobals = enableGlobals;
            return this;
        }

        public ShellEngine.ShellEngineBuilder enableRegister(boolean enableRegister) {
            this.enableRegister = enableRegister;
            return this;
        }

        public ShellEngine.ShellEngineBuilder enableLoad(boolean enableLoad) {
            this.enableLoad = enableLoad;
            return this;
        }

        public ShellEngine.ShellEngineBuilder builtinPackage(String builtinPackage) {
            this.builtinPackage = builtinPackage;
            return this;
        }

        public ShellEngine.ShellEngineBuilder securityAgent(Class<? extends PermissiveUser> securityAgent) {
            this.securityAgent = securityAgent;
            return this;
        }

        public ShellEngine build() throws LoadingBuildinException {
            return new ShellEngine(
                    defaultExpirationTime,
                    defaultConstExpirationTime,
                    disableExpiration,
                    allowConstant,
                    verbose,
                    enableGlobals,
                    enableRegister,
                    enableLoad,
                    builtinPackage,
                    securityAgent
            );
        }
    }
}
