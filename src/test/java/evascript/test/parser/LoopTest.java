package evascript.test.parser;

import dev.kobura.evascript.lexer.EvaLexer;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.EvaParser;
import dev.kobura.evascript.parsing.ast.statement.BlockStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LoopTest {

    public List<Token> tokens(String in) {
        EvaLexer lexer = new EvaLexer(in);
        return lexer.readAll();
    }

    @Test
    void forTest() {
        EvaParser parser = new EvaParser(tokens("for(let i : range(10)) { console.log('Hello World!'); }"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

    @Test
    void completeForTest() {
        EvaParser parser = new EvaParser(tokens("var i = [14, 56, 78]; var z = []; for(let a:i) { z.append(a + 20);"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

    @Test
    void forEachTest() {
        EvaParser parser = new EvaParser(tokens("var i = [14, 56, 78]; var z = []; i.foreach(func (a) { z.append(a + 20); });"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

    @Test
    void whileTest() {
        EvaParser parser = new EvaParser(tokens("while(true) { console.log('Hello World!'); }"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

    @Test
    void doWhileTest() {
        EvaParser parser = new EvaParser(tokens("do { console.log('Hello World!'); } while(true);"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

    @Test
    void breakTest() {
        EvaParser parser = new EvaParser(tokens("while(true) { console.log('Hello World!'); break; }"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

    @Test
    void continueTest() {
        EvaParser parser = new EvaParser(tokens("while(true) { console.log('Hello World!'); continue; }"));
        assertDoesNotThrow(parser::parseAsBlock);
    }


}
