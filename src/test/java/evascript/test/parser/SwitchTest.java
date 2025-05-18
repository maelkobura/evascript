package evascript.test.parser;

import dev.kobura.evascript.lexer.EvaLexer;
import dev.kobura.evascript.lexer.token.Token;
import dev.kobura.evascript.parsing.EvaParser;
import dev.kobura.evascript.parsing.ast.statement.BlockStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SwitchTest {

    public List<Token> tokens(String in) {
        EvaLexer lexer = new EvaLexer(in);
        return lexer.readAll();
    }

    @Test
    void switchTest() {
        EvaParser parser = new EvaParser(tokens("switch(true) { " +
                "case true: console.log('Hello World!'); " +
                "break; " +
                "}"));
        BlockStatement statement = parser.parseAsBlock();
        System.out.println(statement);
    }

}
