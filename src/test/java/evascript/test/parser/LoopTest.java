package evascript.test.parser;

import dev.kobura.evascript.lexer.EvaLexer;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.EvaParser;
import dev.kobura.evascript.parsing.ast.statement.BlockStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

public class LoopTest {

    public List<Token> tokens(String in) {
        EvaLexer lexer = new EvaLexer(in);
        return lexer.readAll();
    }

    @Test
    void forTest() {
        EvaParser parser = new EvaParser(tokens("for(let i : range(10)) { console.log('Hello World!'); }"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void completeForTest() {
        EvaParser parser = new EvaParser(tokens("var i = [14, 56, 78]; var z = []; for(let a:i) { z.append(a + 20);"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void forEachTest() {
        EvaParser parser = new EvaParser(tokens("var i = [14, 56, 78]; var z = []; i.foreach(func (a) { z.append(a + 20); });"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void whileTest() {
        EvaParser parser = new EvaParser(tokens("while(true) { console.log('Hello World!'); }"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void doWhileTest() {
        EvaParser parser = new EvaParser(tokens("do { console.log('Hello World!'); } while(true);"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void breakTest() {
        EvaParser parser = new EvaParser(tokens("while(true) { console.log('Hello World!'); break; }"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void continueTest() {
        EvaParser parser = new EvaParser(tokens("while(true) { console.log('Hello World!'); continue; }"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }


}
