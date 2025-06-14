package dev.kobura.evascript.runtime.value;

import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.ContextData;
import dev.kobura.evascript.runtime.context.Scriptable;
import dev.kobura.evascript.security.PermissiveUser;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JavaObjectValue extends Value {

    private Object object;

    public JavaObjectValue(Object object) {
        this.object = object;
    }

    @Override
    public Object unwrap() {
        return object;
    }

    @Override
    public ValueType getType() {
        return ValueType.OBJECT;
    }

    @Override
    public String toString() {
        try {
            if(object.getClass().getMethod("toString").isAnnotationPresent(Scriptable.class)) {
                return object.toString();
            }else if(!object.getClass().getAnnotation(Scriptable.class).defaultName().equals("")) {
                return "java:" + object.getClass().getAnnotation(Scriptable.class).defaultName();
            }
        } catch (NoSuchMethodException e) {}
        return "java:" + object.getClass().getSimpleName();
    }

    @Override
    public boolean isEqual(Value other) {
        if(other instanceof JavaObjectValue o) {
            return o.object == object;
        }
        return false;
    }

    @Override
    public Value getField(Execution execution, PermissiveUser user, String name) throws RuntimeError {
        Class<?> objectClass = object.getClass();
        Field field;

        try {
            field = objectClass.getField(name);
        } catch (NoSuchFieldException e) {
            return UndefinedValue.INSTANCE;
        }

        if (!field.canAccess(object) || !field.isAnnotationPresent(Scriptable.class)) {
            return UndefinedValue.INSTANCE;
        }

        if(!execution.getEngine().checkPermissions(user, field.getAnnotation(Scriptable.class).permissions())) {
            return UndefinedValue.INSTANCE;
        }

        Object fieldValue;
        try {
            fieldValue = field.get(object);
        } catch (IllegalAccessException e) {
            return UndefinedValue.INSTANCE;
        }

        return Value.from(fieldValue);
    }

    @Override
    public Value execute(Execution execution, PermissiveUser user, String methodName, Value args) throws RuntimeError {

        Method method = Arrays.stream(object.getClass().getMethods())
                .filter(mtd -> mtd.getName().equals(methodName))
                .findAny()
                .orElseThrow(() -> new RuntimeError("Cannot execute method '" + methodName + "' of " + getType().toString()));

        if (!method.canAccess(object) || !method.isAnnotationPresent(Scriptable.class)) {
            return super.execute(execution, user, methodName, args);
        }

        if (!execution.getEngine().checkPermissions(user, method.getAnnotation(Scriptable.class).permissions())) {
            return UndefinedValue.INSTANCE;
        }

        List<Object> javaArgs = new ArrayList<>();

        if (args.getType() == ValueType.ARRAY) {
            Object[] obj = (Object[]) args.unwrap();
            int i = 0;
            for(Parameter param : method.getParameters()) {
                if(param.isAnnotationPresent(ContextData.class)) {
                    if(param.getName().equals("user") && param.getType().equals(execution.getEngine().getSecurityAgent())) {
                        javaArgs.add(execution.getEngine().getSecurityAgent().cast(user));
                    }else {
                        javaArgs.add(execution.getContextData().get(param.getName()));
                    }

                }else {
                    if (param.getType().isAssignableFrom(String[].class)) {
                        Object value = obj[i];

                        if (value instanceof Object[]) {
                            Object[] objectArray = (Object[]) value;

                            // Conversion en String[]
                            String[] stringArray = Arrays.stream(objectArray)
                                    .map(element -> element != null ? element.toString() : null)
                                    .toArray(String[]::new);

                            javaArgs.add(stringArray);
                        } else {
                            javaArgs.add(null); // ou gérer le cas d'erreur différemment
                        }
                    }
                    else {
                        javaArgs.add(obj[i]);
                    }
                    i++;
                }
            }
        }else if(args.getType() == ValueType.OBJECT) {
            Map<String, Object> obj = (Map<String, Object>) args.unwrap();
            for(Parameter param : method.getParameters()) {
                if(param.isAnnotationPresent(ContextData.class)) {
                    if(param.getName().equals("user") && param.getType().equals(execution.getEngine().getSecurityAgent())) {
                        javaArgs.add(execution.getEngine().getSecurityAgent().cast(user));
                    }else {
                        javaArgs.add(execution.getContextData().get(param.getName()));
                    }
                }else {
                    if (param.getType().isAssignableFrom(String[].class)) {
                        Object value = obj.getOrDefault(param.getName(), null);

                        if (value instanceof Object[]) {
                            Object[] objectArray = (Object[]) value;

                            // Conversion en String[]
                            String[] stringArray = Arrays.stream(objectArray)
                                    .map(element -> element != null ? element.toString() : null)
                                    .toArray(String[]::new);

                            javaArgs.add(stringArray);
                        } else {
                            javaArgs.add(null); // ou gérer le cas d'erreur différemment
                        }
                    }
                    else {
                        javaArgs.add(obj.getOrDefault(param.getName(), null));
                    }

                }
            }
        }

        try {
            Object result = method.invoke(object, javaArgs.toArray());
            return Value.from(result);
        } catch (IllegalAccessException e) {
            return super.execute(execution, user, methodName, args);
        } catch (Exception e) {
            if(e instanceof InvocationTargetException && ((InvocationTargetException) e).getTargetException() instanceof RuntimeError err) {
                throw err;
            } else if (e instanceof InvocationTargetException) {
                throw new RuntimeError("Native method '" + methodName + "' threw an exception: " +
                        ((InvocationTargetException) e).getTargetException().getMessage(), ((InvocationTargetException) e).getTargetException());
            } else {
                throw new RuntimeError("Native method '" + methodName + "' threw an exception: " +
                        e, e);
            }
        }
    }
    
}
