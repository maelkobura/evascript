package dev.kobura.evascript;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.ContextIdentity;
import dev.kobura.evascript.runtime.context.Scope;
import dev.kobura.evascript.runtime.context.registerable.Register;
import dev.kobura.evascript.runtime.context.registerable.RegisteredFunction;
import dev.kobura.evascript.runtime.value.Value;
import dev.kobura.evascript.security.PermissiveUser;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface ScriptEngine {

    long getDefaultExpirationTime();

    long getDefaultConstExpirationTime();

    boolean isDisableExpiration();

    boolean isAllowConstant();

    boolean isVerbose();

    boolean isEnableGlobals();

    boolean isEnableRegister();

    boolean isEnableLoad();

    String getBuiltinPackage();

    Value invokeSystem(Execution execution, String name, Value...values) throws RuntimeError;

    void systemMethod(RegisteredFunction function);

    Class<? extends PermissiveUser> getSecurityAgent();

    Value getBuiltin(String name);

    Map<ContextIdentity, Value> getAllBuiltin();

    void register(Register register);

    String run(String code, Scope scope, PermissiveUser user) throws RuntimeError;

    String run(File file, Scope scope, PermissiveUser user) throws RuntimeError, IOException;

    boolean checkPermissions(PermissiveUser user, String...permissions);

    Scope createScope();


}
