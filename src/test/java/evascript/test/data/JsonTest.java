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
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
