package dev.kobura.evascript.runtime.value;

import com.google.gson.*;
import dev.kobura.evascript.runtime.Execution;
import dev.kobura.evascript.runtime.context.Scriptable;
import dev.kobura.evascript.security.PermissiveUser;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;

public class ObjectValue extends Value {

    @Getter
    Map<String, Value> values;

    private static final Gson gson = new Gson();

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

    public static ObjectValue fromMap(Map<String, Object> map) {
        return new ObjectValue(map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> Value.from(entry.getValue())
                )));
    }



    public static ObjectValue fromJson(JsonObject json) {
        Map<String, Value> map = json.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> fromJsonElement(entry.getValue())
                ));
        return new ObjectValue(map);
    }

    private static Value fromJsonElement(JsonElement element) {
        if (element.isJsonNull()) {
            return UndefinedValue.INSTANCE;
        } else if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isBoolean()) {
                return Value.from(primitive.getAsBoolean());
            } else if (primitive.isNumber()) {
                return Value.from(primitive.getAsDouble());
            } else if (primitive.isString()) {
                return Value.from(primitive.getAsString());
            }
        } else if (element.isJsonObject()) {
            return fromJson(element.getAsJsonObject());
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            Value[] values = new Value[array.size()];
            for (int i = 0; i < array.size(); i++) {
                values[i] = fromJsonElement(array.get(i));
            }
            return Value.from(values);
        }
        return UndefinedValue.INSTANCE;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        for (Map.Entry<String, Value> entry : values.entrySet()) {
            json.add(entry.getKey(), toJsonElement(entry.getValue()));
        }
        return json;
    }

    private static JsonElement toJsonElement(Value value) {
        if (value instanceof StringValue s) {
            return new JsonPrimitive(String.valueOf(s.unwrap()));
        } else if (value instanceof NumberValue n) {
            return new JsonPrimitive((Number) n.unwrap());
        } else if (value instanceof BooleanValue b) {
            return new JsonPrimitive((Boolean) b.unwrap());
        } else if (value instanceof ArrayValue a) {
            JsonArray array = new JsonArray();
            for (Value v : a.getValues()) {
                array.add(toJsonElement(v));
            }
            return array;
        } else if (value instanceof ObjectValue o) {
            return o.toJson(); // récursif
        } else if (value instanceof UndefinedValue) {
            return null; // ou JsonNull.INSTANCE si tu préfères explicitement JsonNull
        } else {
            return new JsonPrimitive(value.toString()); // fallback
        }
    }

    @Override
    public ValueType getType() {
        return ValueType.OBJECT;
    }

    @Scriptable
    public Value keys() {
        return Value.from(values.keySet().toArray(new String[0]));
    }

    @Scriptable
    public Value values() {
        return Value.from(values.values().toArray(new Value[0]));
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
        return gson.toJsonTree(values).toString();
    }

}
