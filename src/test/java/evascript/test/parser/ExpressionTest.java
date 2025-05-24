package evascript.test.parser;

import dev.kobura.evascript.lexer.EvaLexer;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.EvaParser;
import dev.kobura.evascript.parsing.ast.statement.BlockStatement;
import dev.kobura.evascript.parsing.ast.statement.ExpressionStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ExpressionTest {

    public List<Token> tokens(String in) {
        EvaLexer lexer = new EvaLexer(in);
        return lexer.readAll();
    }

    @Test
    void binaryAddition() {
        EvaParser parser = new EvaParser(tokens("1 + 1"));
        assertDoesNotThrow(parser::parseAsExpression);
    }


    @Test
    void binaryMultiplication() {
        EvaParser parser = new EvaParser(tokens("2 * 3"));
        assertDoesNotThrow(parser::parseAsExpression);
    }

    @Test
    void binaryDivision() {
        EvaParser parser = new EvaParser(tokens("4 / 2"));
        assertDoesNotThrow(parser::parseAsExpression);
    }

    @Test
    void binaryModulo() {
        EvaParser parser = new EvaParser(tokens("4 % 2"));
        assertDoesNotThrow(parser::parseAsExpression);
    }

    @Test
    void binarySubtraction() {
        EvaParser parser = new EvaParser(tokens("4 - 2"));
        assertDoesNotThrow(parser::parseAsExpression);
    }


    @Test
    void binaryMathExpression() {
        EvaParser parser = new EvaParser(tokens("( 3 + 4 ) * 2"));
        assertDoesNotThrow(parser::parseAsExpression);
    }


    @Test
    void binaryMoreComplicatedMathExpression() {
        EvaParser parser = new EvaParser(tokens("( 3 + 4 ) * 2 + 10 - 5 * 2"));
        assertDoesNotThrow(parser::parseAsExpression);
    }

    @Test
    void unaryMinus() {
        EvaParser parser = new EvaParser(tokens("a--"));
        assertDoesNotThrow(parser::parseAsExpression);
    }

    @Test
    void unaryPlus() {
        EvaParser parser = new EvaParser(tokens("a++"));
        assertDoesNotThrow(parser::parseAsExpression);
    }

    @Test
    void unaryNot() {
        EvaParser parser = new EvaParser(tokens("!a"));
        assertDoesNotThrow(parser::parseAsExpression);
    }

    @Test
    void functionCall() {
        EvaParser parser = new EvaParser(tokens("system.log('Hello World!')"));
        assertDoesNotThrow(parser::parseAsExpression);
    }

    @Test
    void variable() {
        EvaParser parser = new EvaParser(tokens("a"));
        assertDoesNotThrow(parser::parseAsExpression);
    }

    @Test
    void number() {
        EvaParser parser = new EvaParser(tokens("10"));
        assertDoesNotThrow(parser::parseAsExpression);
    }

    @Test
    void string() {
        EvaParser parser = new EvaParser(tokens("\"Hello World!\""));
        assertDoesNotThrow(parser::parseAsExpression);
    }

    @Test
    void bool() {
        EvaParser parser = new EvaParser(tokens("true"));
        assertDoesNotThrow(parser::parseAsExpression);
    }

    @Test
    void contextAccess() {
        EvaParser parser = new EvaParser(tokens("a = system.randomData"));
        assertDoesNotThrow(parser::parseAsExpression);
    }

}
