package evascript.test.lexer;

import gg.kobuz.evascript.lexer.EvaSyntax;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SyntaxTest {

    @Test
    void preprocessTest() {
        assertEquals("Hello world!", EvaSyntax.preprocessing("Hello {{dlrow}}!", s -> {
            return new StringBuilder(s).reverse().toString();
        }));
    }

}
