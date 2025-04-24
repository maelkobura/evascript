package evascript.test.parser;

import gg.kobuz.evascript.lexer.EvaLexer;
import gg.kobuz.evascript.lexer.token.Token;
import gg.kobuz.evascript.runtime.parsing.EvaParser;
import gg.kobuz.evascript.runtime.parsing.ast.statement.BlockStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

public class StatementTest {

    public List<Token> tokens(String in) {
        EvaLexer lexer = new EvaLexer(in);
        return lexer.readAll();
    }

    @Test
    void ifTest() {
        EvaParser parser = new EvaParser(tokens("if (true) { console.log('Hello World!'); }"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void expressionTest() {
        EvaParser parser = new EvaParser(tokens("console.log('Hello World!')"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void forTest() {
        EvaParser parser = new EvaParser(tokens("for(let i : range(10)) { console.log('Hello World!'); }"));
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
    void lambdaTest() {
        EvaParser parser = new EvaParser(tokens("let value = { console.log('Hello World!'); }"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

}
