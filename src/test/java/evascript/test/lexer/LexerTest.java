package evascript.test.lexer;

import dev.kobura.evascript.lexer.EvaLexer;
import dev.kobura.evascript.lexer.token.SyntaxToken;
import dev.kobura.evascript.lexer.token.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {


    @Test
    void testExtendedLexer() {
        EvaLexer lexer = new EvaLexer("if (true) { console.log('Hello World!') }");
        Token token = lexer.readToken();
        assertEquals(SyntaxToken.IF, token.type);

        token = lexer.readToken();
        assertEquals(SyntaxToken.LPAREN, token.type);

        token = lexer.readToken();
        assertEquals(SyntaxToken.BOOLEAN, token.type);

        token = lexer.readToken();
        assertEquals(SyntaxToken.RPAREN, token.type);

        token = lexer.readToken();
        assertEquals(SyntaxToken.LBRACE, token.type);

        token = lexer.readToken();
        assertEquals(SyntaxToken.IDENTIFIER, token.type);
        assertEquals("console", token.value);

        token = lexer.readToken();
        assertEquals(SyntaxToken.DOT, token.type);

        token = lexer.readToken();
        assertEquals(SyntaxToken.IDENTIFIER, token.type);
        assertEquals("log", token.value);

        token = lexer.readToken();
        assertEquals(SyntaxToken.LPAREN, token.type);

        token = lexer.readToken();
        assertEquals(SyntaxToken.STRING, token.type);
        assertEquals("Hello World!", token.value);

        token = lexer.readToken();
        assertEquals(SyntaxToken.RPAREN, token.type);

        token = lexer.readToken();
        assertEquals(SyntaxToken.RBRACE, token.type);
    }

    @Test
    void testAutomaticLexer() {
        EvaLexer lexer = new EvaLexer("if (true) {console.log('Hello World!');}");
        List<Token> token = lexer.readAll();
        token.forEach(System.out::println);
        assertEquals(13, token.size());
    }

}
