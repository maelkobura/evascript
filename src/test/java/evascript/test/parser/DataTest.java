package evascript.test.parser;

import dev.kobura.evascript.lexer.EvaLexer;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.EvaParser;
import dev.kobura.evascript.parsing.ast.statement.BlockStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class DataTest {

    public List<Token> tokens(String in) {
        EvaLexer lexer = new EvaLexer(in);
        return lexer.readAll();
    }

    @Test
    void emptyData() {
        EvaParser parser = new EvaParser(tokens("let a = {};"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

    @Test
    void simpleData() {
        EvaParser parser = new EvaParser(tokens("let a = {hello: 'world'};"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

    @Test
    void complexData() {
        EvaParser parser = new EvaParser(tokens("let a = {hello: 'world',14: 'world',[System.call()]:45};"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

    @Test
    void nestedData() {
        EvaParser parser = new EvaParser(tokens("let a = {hello: 'world',14: {hello: 'world',14: 'world',[System.call()]:45}};"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

    @Test
    void simpleArray() {
        EvaParser parser = new EvaParser(tokens("let a = [1,2,3];"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

    @Test
    void simpleArrayAccess() {
        EvaParser parser = new EvaParser(tokens("let a = b[14];"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

    @Test
    void expressionArrayAccess() {
        EvaParser parser = new EvaParser(tokens("let a = b[System.call()];"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

    @Test
    void strageArrayAccess() {
        EvaParser parser = new EvaParser(tokens("let a = [1,2,3][System.call()];"));
        assertDoesNotThrow(parser::parseAsBlock);
    }

}
