package evascript.test.parser;

import dev.kobura.evascript.lexer.EvaLexer;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.EvaParser;
import dev.kobura.evascript.parsing.ast.statement.BlockStatement;
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
        EvaParser parser = new EvaParser(tokens("let value = func async () { console.log('Hello World!'); }"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }


    @Test
    void lambdaArgsTest() {
        EvaParser parser = new EvaParser(tokens("let value = func async (message) { console.log(message); }"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void funcTest() {
        EvaParser parser = new EvaParser(tokens("func hello() { console.log('Hello World!'); }"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void funcArgsTest() {
        EvaParser parser = new EvaParser(tokens("func hello(message) { console.log(message); }"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void tryCatchTest() {
        EvaParser parser = new EvaParser(tokens("try { console.log('Trying...'); } catch(e) { console.log(e); }"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void tryCatchFinallyTest() {
        EvaParser parser = new EvaParser(tokens("try { console.log('Trying...'); } catch(e) { console.log(e); } finally { console.log('Finished.'); }"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

}
