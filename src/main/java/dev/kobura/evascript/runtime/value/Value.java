package dev.kobura.evascript.runtime.value;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.ContextData;
import dev.kobura.evascript.runtime.context.Scriptable;
import dev.kobura.evascript.security.PermissiveUser;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Value {

    public abstract Object unwrap();
    public abstract ValueType getType();
    public abstract boolean isEqual(Value other);

    @Scriptable
    public String toString() {
        return getType().toString();
    };

    public Value getField(Execution execution, PermissiveUser user, String name) throws RuntimeError {
        return UndefinedValue.INSTANCE;
    }
    public void setField(Execution execution, PermissiveUser user, String name, Value value) throws RuntimeError {
        throw new RuntimeError("Cannot set property '" + name + "' of " + getType().toString());
    }

    public Value execute(Execution execution, PermissiveUser user, String methodName, Value args) throws RuntimeError {

        for(Method mtd : getClass().getMethods()) {
            if(mtd.isAnnotationPresent(Scriptable.class) && mtd.getName().equals(methodName)) {

                if (!execution.getEngine().checkPermissions(user, mtd.getAnnotation(Scriptable.class).permissions())) {
                    break;
                }

                List<Object> javaArgs = new ArrayList<>();

                if (args.getType() == ValueType.ARRAY) {
                    List<Value> obj = ((ArrayValue) args).getValues();
                    int i = 0;
                    for(Parameter param : mtd.getParameters()) {
                        if(param.isAnnotationPresent(ContextData.class)) {
                            System.out.println(param.getName());
                            javaArgs.add(execution.getContextData().get(param.getName()));
                        }else {
                            javaArgs.add(param.getType().equals(Value.class) ? obj.get(i) : obj.get(i).unwrap());
                            i++;
                        }
                    }
                }else if(args.getType() == ValueType.OBJECT) {
                    Map<String, Object> obj = (Map<String, Object>) args.unwrap();
                    for(Parameter param : mtd.getParameters()) {
                        if(param.isAnnotationPresent(ContextData.class)) {
                            javaArgs.add(execution.getContextData().get(param.getName()));
                        }else {
                            javaArgs.add(obj.get(param.getName()));
                        }
                    }
                }
                try {
                    Object result = mtd.invoke(this, javaArgs.toArray());
                    return Value.from(result);
                } catch (IllegalAccessException e) {
                    break;
                } catch (Exception e) {
                    throw new RuntimeError("Native method '" + methodName + "' threw an exception: " +
                            e.getMessage(), e);
                }
            }
        }

        throw new RuntimeError("Cannot execute method '" + methodName + "' of " + getType().toString());
    }

    public Value execute(Execution execution, PermissiveUser user, Value args) throws RuntimeError {
        throw new RuntimeError("Cannot execute the object " + getType().toString());
    }

    public Value add(Value other)throws RuntimeError {
        return NaNValue.INSTANCE;
    }
    public Value subtract(Value other) throws RuntimeError {
        return NaNValue.INSTANCE;
    }
    public Value multiply(Value other)throws RuntimeError {
        return NaNValue.INSTANCE;
    }
    public Value divide(Value other)throws RuntimeError {
        return NaNValue.INSTANCE;
    }
    public Value modulo(Value other)throws RuntimeError {
        return NaNValue.INSTANCE;
    }

    public static Value from(Object o) {

        if (o == null) {return NullValue.INSTANCE;}

        if (o instanceof String stringValue) {
            return new StringValue(stringValue);
        } else if (o instanceof Number numberValue) {
            return new NumberValue(numberValue.doubleValue());
        } else if (o instanceof Boolean booleanValue) {
            return new BooleanValue(booleanValue);
        } else if (o.getClass().isAnnotationPresent(Scriptable.class)) {
            return new JavaObjectValue(o);
        } else if (o instanceof Value value) {
            return value;
        } else if (o instanceof Object[]) {
            // TODO: Handle arrays
            return UndefinedValue.INSTANCE;
        } else {
            return NullValue.INSTANCE;
        }
    }

}
