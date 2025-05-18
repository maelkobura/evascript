package evascript.test.lexer;

import dev.kobura.evascript.lexer.EvaSyntax;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SyntaxTest {

    @Test
    void preprocessTest() {
        String code = "<?\n" +
                "let cat = cat.myCat()\n" +
                ">\n" +
                "Your cat name is {cat.name}.";

        EvaSyntax.ParseResult result = EvaSyntax.preprocess(code);
        assertEquals("let cat = cat.myCat()", result.root);
        assertEquals(List.of("cat.name"), result.embeds);
    }

    @Test
    void noRootTest() {
        String code = "Your cat name is {cat.name}.";

        EvaSyntax.ParseResult result = EvaSyntax.preprocess(code);
        assertNull(result.root);
        assertEquals(List.of("cat.name"), result.embeds);
    }

    @Test
    void onlyRootTest() {
        String code = "<?\n" +
                "let cat = cat.myCat()\n" +
                ">";

        EvaSyntax.ParseResult result = EvaSyntax.preprocess(code);
        assertEquals("let cat = cat.myCat()", result.root);
        assertEquals(List.of(), result.embeds);
    }


    @Test
    void multipleRootsTest() {
        String code = "<?\nlet a = 1\n><?\nlet b = 2\n>";

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> EvaSyntax.preprocess(code));
        assertEquals("Multiple root blocks found in input.", exception.getMessage());
    }

    @Test
    void noRootNoEmbedsTest() {
        String code = "This is a simple text without root or embeds.";

        EvaSyntax.ParseResult result = EvaSyntax.preprocess(code);
        assertEquals(null, result.root);
        assertEquals(List.of(), result.embeds);
    }

    @Test
    void rootWithNestedEmbedsTest() {
        String code = "<?\nlet user = getUser()\n>\nWelcome {user.name} to {user.details.location.city}.";

        EvaSyntax.ParseResult result = EvaSyntax.preprocess(code);
        assertEquals("let user = getUser()", result.root);
        assertEquals(List.of("user.name", "user.details.location.city"), result.embeds);
    }

    @Test
    void specialCharactersTest() {
        String code = "<?\nlet message = \"Hello, World!\"\n>\nGreetings: {message}, have a nice day!";

        EvaSyntax.ParseResult result = EvaSyntax.preprocess(code);
        assertEquals("let message = \"Hello, World!\"", result.root);
        assertEquals(List.of("message"), result.embeds);
    }

    @Test
    void reassembleTest() {
        String originalInput = "<?\nlet cat = cat.myCat()\n>\nYour cat name is {cat.name}.";
        EvaSyntax.ParseResult result = EvaSyntax.preprocess(originalInput);

        String reassembled = result.reassemble(List.of("Fluffy"));
        assertEquals("Your cat name is Fluffy.", reassembled);
    }

    @Test
    void reassembleNoEmbedsTest() {
        String originalInput = "This is a text without any embed blocks.";
        EvaSyntax.ParseResult result = EvaSyntax.preprocess(originalInput);

        String reassembled = result.reassemble(List.of());
        assertEquals("This is a text without any embed blocks.", reassembled);
    }

    @Test
    void reassembleMismatchTest() {
        String originalInput = "<?\nlet cat = cat.myCat()\n>\nYour cat name is {cat.name}.";
        EvaSyntax.ParseResult result = EvaSyntax.preprocess(originalInput);

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> result.reassemble(List.of("Fluffy", "Spot"))
        );

        assertEquals("Number of new embeds must match the number of original embeds.", exception.getMessage());
    }

}
