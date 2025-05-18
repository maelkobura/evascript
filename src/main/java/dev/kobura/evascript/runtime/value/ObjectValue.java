package dev.kobura.evascript.runtime.value;

import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.security.PermissiveUser;

import java.util.Map;
import java.util.stream.Collectors;

public class ObjectValue extends Value {

    Map<String, Value> values;

    public Map<String, Object> unwrap() {
        return values.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().unwrap()
                ));
    }

    public ObjectValue(Map<String, Value> values) {
        this.values = values;
    }



    @Override
    public ValueType getType() {
        return ValueType.OBJECT;
    }

    @Override
    public boolean isEqual(Value other) {
        if (other instanceof ObjectValue o) {
            for(Map.Entry<String, Value> entry : values.entrySet()) {
                if(!o.values.containsKey(entry.getKey())) {
                    return false;
                }else if(!o.values.get(entry.getKey()).isEqual(entry.getValue())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Value getField(Execution execution, PermissiveUser user, String name) {
        return values.getOrDefault(name, UndefinedValue.INSTANCE);
    }

    @Override
    public void setField(Execution execution, PermissiveUser user, String name, Value value) {
        values.put(name, value);
    }

    @Override
    public String toString() {
        return values.toString();
    }

}
