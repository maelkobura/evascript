package evascript.test.parser;

import dev.kobura.evascript.lexer.EvaLexer;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.EvaParser;
import dev.kobura.evascript.parsing.ast.statement.BlockStatement;
import dev.kobura.evascript.parsing.ast.statement.ExpressionStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ExpressionTest {

    public List<Token> tokens(String in) {
        EvaLexer lexer = new EvaLexer(in);
        return lexer.readAll();
    }

    @Test
    void binaryAddition() {
        EvaParser parser = new EvaParser(tokens("1 + 1"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }


    @Test
    void binaryMultiplication() {
        EvaParser parser = new EvaParser(tokens("2 * 3"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }

    @Test
    void binaryDivision() {
        EvaParser parser = new EvaParser(tokens("4 / 2"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }

    @Test
    void binaryModulo() {
        EvaParser parser = new EvaParser(tokens("4 % 2"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }

    @Test
    void binarySubtraction() {
        EvaParser parser = new EvaParser(tokens("4 - 2"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }


    @Test
    void binaryMathExpression() {
        EvaParser parser = new EvaParser(tokens("( 3 + 4 ) * 2"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }


    @Test
    void binaryMoreComplicatedMathExpression() {
        EvaParser parser = new EvaParser(tokens("( 3 + 4 ) * 2 + 10 - 5 * 2"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }

    @Test
    void unaryMinus() {
        EvaParser parser = new EvaParser(tokens("a--"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }

    @Test
    void unaryPlus() {
        EvaParser parser = new EvaParser(tokens("a++"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }

    @Test
    void unaryNot() {
        EvaParser parser = new EvaParser(tokens("!a"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }

    @Test
    void functionCall() {
        EvaParser parser = new EvaParser(tokens("system.log('Hello World!')"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }

    @Test
    void variable() {
        EvaParser parser = new EvaParser(tokens("a"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }

    @Test
    void number() {
        EvaParser parser = new EvaParser(tokens("10"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }

    @Test
    void string() {
        EvaParser parser = new EvaParser(tokens("\"Hello World!\""));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }

    @Test
    void bool() {
        EvaParser parser = new EvaParser(tokens("true"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }

    @Test
    void contextAccess() {
        EvaParser parser = new EvaParser(tokens("a = system.randomData"));
        ExpressionStatement statement = parser.parseAsExpression();
        System.out.println(statement);
    }

}
