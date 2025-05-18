package evascript.test.parser;

import dev.kobura.evascript.lexer.EvaLexer;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.EvaParser;
import dev.kobura.evascript.parsing.ast.statement.BlockStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DataTest {

    public List<Token> tokens(String in) {
        EvaLexer lexer = new EvaLexer(in);
        return lexer.readAll();
    }

    @Test
    void emptyData() {
        EvaParser parser = new EvaParser(tokens("let a = {};"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void simpleData() {
        EvaParser parser = new EvaParser(tokens("let a = {hello: 'world'};"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void complexData() {
        EvaParser parser = new EvaParser(tokens("let a = {hello: 'world',14: 'world',[System.call()]:45};"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void nestedData() {
        EvaParser parser = new EvaParser(tokens("let a = {hello: 'world',14: {hello: 'world',14: 'world',[System.call()]:45}};"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void simpleArray() {
        EvaParser parser = new EvaParser(tokens("let a = [1,2,3];"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void simpleArrayAccess() {
        EvaParser parser = new EvaParser(tokens("let a = b[14];"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void expressionArrayAccess() {
        EvaParser parser = new EvaParser(tokens("let a = b[System.call()];"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

    @Test
    void strageArrayAccess() {
        EvaParser parser = new EvaParser(tokens("let a = [1,2,3][System.call()];"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

}
