package evascript.test.parser;

import dev.kobura.evascript.lexer.EvaLexer;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.EvaParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ArgumentsTest {

    public List<Token> tokens(String in) {
        EvaLexer lexer = new EvaLexer(in);
        return lexer.readAll();
    }

    @Test
    void sequencedArgsTest() {
        EvaParser parser = new EvaParser(tokens("let value = system.call(1, 2, 3)"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

    @Test
    void structuredArgsTest() {
        EvaParser parser = new EvaParser(tokens("let value = system.call(first=1, second=2, third=3)"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

}
