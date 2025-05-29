package evascript.test.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.kobura.evascript.engine.EngineFactory;
import dev.kobura.evascript.engine.ScriptEngine;
import dev.kobura.evascript.errors.LoadingBuildinException;
import dev.kobura.evascript.errors.RuntimeError;
import dev.kobura.evascript.runtime.context.ContextIdentity;
import dev.kobura.evascript.runtime.context.Scope;
import dev.kobura.evascript.runtime.value.ObjectValue;
import dev.kobura.evascript.runtime.value.UndefinedValue;
import dev.kobura.evascript.runtime.value.Value;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JsonTest {

    @Test
    void fromJson() throws LoadingBuildinException, RuntimeError {
        ScriptEngine engine = EngineFactory.createShellEngine().build();
        Scope scope = engine.createScope();

        JsonObject json = new JsonObject();

        // Primitives
        json.addProperty("string", "hello");
        json.addProperty("number", 42);
        json.addProperty("bool", true);
        json.add("null", null); // sera interprété comme undefined

        // Array
        JsonArray array = new JsonArray();
        array.add(45);
        array.add(23);
        array.add(12);
        json.add("array", array);

        // Nested object
        JsonObject nested = new JsonObject();
        nested.addProperty("nestedKey", "nestedValue");
        json.add("object", nested);

        // Création de l’ObjectValue depuis le JSON
        ObjectValue value = ObjectValue.fromJson(json);

        // Ajout dans le scope
        scope.set(new ContextIdentity("a", false, Instant.now(), 0), value);

        // Vérification des primitives
        assertEquals("hello", engine.run("a.string", scope, null));
        assertEquals("42", engine.run("a.number", scope, null));
        assertEquals("true", engine.run("a.bool", scope, null));
        assertEquals("undefined", engine.run("a.null", scope, null));

        // Vérification du tableau
        assertEquals("[45, 23, 12]", engine.run("a.array", scope, null));

        // Vérification de l'objet imbriqué
        assertEquals("nestedValue", engine.run("a.object.nestedKey", scope, null));
    }

    @Test
    void toJson() {
        Map<String, Value> values = Map.of(
                "string", Value.from("hello"),
                "number", Value.from(42),
                "bool", Value.from(true),
                "null", UndefinedValue.INSTANCE,
                "array", Value.from(new Value[] {
                        Value.from(45),
                        Value.from(23),
                        Value.from(12)
                }),
                "object", new ObjectValue(Map.of(
                        "nestedKey", Value.from("nestedValue")
                ))
        );

        ObjectValue objectValue = new ObjectValue(values);
        JsonObject json = objectValue.toJson();

        assertEquals("hello", json.get("string").getAsString());
        assertEquals(42, json.get("number").getAsInt());
        assertEquals(true, json.get("bool").getAsBoolean());

        JsonArray arr = json.getAsJsonArray("array");
        assertEquals(3, arr.size());
        assertEquals(45, arr.get(0).getAsInt());
        assertEquals(23, arr.get(1).getAsInt());
        assertEquals(12, arr.get(2).getAsInt());

        JsonObject nested = json.getAsJsonObject("object");
        assertEquals("nestedValue", nested.get("nestedKey").getAsString());
    }

}
