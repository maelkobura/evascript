package dev.kobura.evascript;

import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.lexer.EvaLexer;
import dev.kobura.evascript.lexer.EvaSyntax;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.EvaParser;
import dev.kobura.evascript.parsing.ast.statement.BlockStatement;
import dev.kobura.evascript.parsing.ast.statement.ExpressionStatement;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.ContextIdentity;
import dev.kobura.evascript.runtime.context.Scope;
import dev.kobura.evascript.runtime.context.Scriptable;
import dev.kobura.evascript.runtime.value.UndefinedValue;
import dev.kobura.evascript.runtime.value.Value;
import dev.kobura.evascript.security.PermissiveUser;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import lombok.Builder;
import lombok.Getter;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder(builderMethodName = "create")
@Getter
public class ScriptEngine {

    @lombok.Builder.Default
    private long defaultExpirationTime = 600000;
    @lombok.Builder.Default
    private long defaultConstExpirationTime = 3600000;
    @lombok.Builder.Default
    private boolean disableExpiration = false;
    @lombok.Builder.Default
    private boolean allowConstant = true;
    @lombok.Builder.Default
    private boolean verbose = false;
    @lombok.Builder.Default
    private boolean enableGlobals = true;
    @lombok.Builder.Default
    private String buildinPackage = "dev.kobura.evascript.globals";
    @lombok.Builder.Default
    private Class<? extends PermissiveUser> securityClass = null;

    @lombok.Builder.Default
    private Map<ContextIdentity, Value> buildin = new HashMap<>();

    public void loadBuildin(String packageName) throws LoadingBuildinException {
        try (ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages(buildinPackage)
                .scan()) {

            for(ClassInfo classInfo : scanResult.getClassesWithAnnotation(Scriptable.class.getName())) {
                if (!classInfo.getOuterClasses().isEmpty()) {
                    continue;
                }
                try {
                    loadBuildinByClass(classInfo.getName());
                }catch (LoadingBuildinException e) {
                    throw e;
                }catch (Exception e) {
                    throw new LoadingBuildinException(e);
                }
            }

        }
    }

    public void loadBuildinByClass(String className) throws ClassNotFoundException, LoadingBuildinException {
        Class<?> clazz = Class.forName(className);
        if(!clazz.isAnnotationPresent(Scriptable.class)) throw new LoadingBuildinException(className + ": the class hasn't Scriptable");
        Scriptable scriptable = clazz.getAnnotation(Scriptable.class);
        if(scriptable.defaultName().isEmpty()) {
            throw new LoadingBuildinException(className + ": the class hasn't default name");
        }
        try {
            Object obj = clazz.getDeclaredConstructor().newInstance();
            ContextIdentity identity = new ContextIdentity(scriptable.defaultName(), true, Instant.now(), 0);
            buildin.put(identity, Value.from(obj));
        } catch (Exception e) {
            throw new LoadingBuildinException(className + ": cannot instantiate the class", e);
        }
    }

    public void loadDefaultBuildin() throws LoadingBuildinException {
        loadBuildin(buildinPackage);
    }

    public Value getBuildIn(ContextIdentity identity) {
        return buildin.getOrDefault(identity, UndefinedValue.INSTANCE);
    }

    public Value getBuildInByName(String name) {
        return buildin.keySet().stream()
                .filter(identity -> identity.getName().equals(name))
                .findFirst()
                .map(buildin::get)
                .orElse(UndefinedValue.INSTANCE);
    }

    public String run(String code, Scope scope, PermissiveUser user) throws RuntimeError {
        EvaSyntax.ParseResult result = EvaSyntax.preprocess(code);
        if(result.root != null) {
            EvaLexer lexer = new EvaLexer(result.root);
            List<Token> tokens = lexer.readAll();

            EvaParser parser = new EvaParser(tokens);
            BlockStatement block = parser.parseAsBlock();

            Execution execution = new Execution(this, scope, user);
            execution.runRoot(block);
        }

        List<String> values = new ArrayList<>();

        for(String query : result.embeds) {
            EvaLexer lexer = new EvaLexer(query);
            List<Token> tokens = lexer.readAll();

            EvaParser parser = new EvaParser(tokens);
            ExpressionStatement stmt = parser.parseAsExpression();
            Execution execution = new Execution(this, scope, user);
            Object value = execution.runEmbed(stmt);
            if(value != null) {
                values.add(value.toString());
            }else {
                values.add("");
            }
        }

        long end = System.nanoTime();
        return result.reassemble(values);
    }

    public String run(File file, Scope scope, PermissiveUser user) throws RuntimeError, IOException {
        FileInputStream in = new FileInputStream(file);
        return run(new String(in.readAllBytes()), scope, user);
    }

    public boolean checkPermissions(PermissiveUser user, String...permissions) {
        if(securityClass == null) return true;
        if(user == null) return false;
        for(String permission : permissions) {
            if(user.hasPermission(permission)) return true;
        }
        return false;
    }

}
