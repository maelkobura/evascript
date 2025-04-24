package gg.kobuz.evascript.runtime.parsing;

import gg.kobuz.evascript.lexer.token.*;
import gg.kobuz.evascript.runtime.parsing.ast.ASTExpression;
import gg.kobuz.evascript.runtime.parsing.ast.ASTStatement;
import gg.kobuz.evascript.runtime.parsing.ast.expression.ForeachExpression;
import gg.kobuz.evascript.runtime.parsing.ast.expression.StatementExpression;
import gg.kobuz.evascript.runtime.parsing.ast.expression.context.MethodCallExpression;
import gg.kobuz.evascript.runtime.parsing.ast.expression.context.variable.args.ArgumentExpression;
import gg.kobuz.evascript.runtime.parsing.ast.expression.LiteralExpression;
import gg.kobuz.evascript.runtime.parsing.ast.expression.context.ContextAccessExpression;
import gg.kobuz.evascript.runtime.parsing.ast.expression.context.variable.VariableDeclaration;
import gg.kobuz.evascript.runtime.parsing.ast.expression.context.variable.args.SequencedArgumentExpression;
import gg.kobuz.evascript.runtime.parsing.ast.expression.context.variable.args.StructuredArgumentExpression;
import gg.kobuz.evascript.runtime.parsing.ast.expression.logical.BinaryExpression;
import gg.kobuz.evascript.runtime.parsing.ast.expression.logical.ComparisonExpression;
import gg.kobuz.evascript.runtime.parsing.ast.statement.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ... imports and package declarations remain unchanged ...

public class EvaParser {

    protected List<Token> tokens;
    protected int current = 0;

    public EvaParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    protected Token currentToken() {
        return tokens.get(current);
    }

    protected boolean isAtEnd() {
        return current >= tokens.size(); // fixed off-by-one error
    }

    private Token peekToken(int offset) {
        if (current + offset >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(current + offset);
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean match(TokenType type, String value) {
        if (check(type) && currentToken().value.equals(value)) {
            advance();
            return true;
        }
        return false;
    }

    protected Token consume(TokenType type, String errorMessage) {
        if (isAtEnd()) return null;
        if (check(type)) return advance();
        throw new RuntimeException("Parse error at token " + currentToken() + ": " + errorMessage + " (index " + current + ", previous token: " + previous() + ")");
    }

    protected boolean check(TokenType type) {
        return !isAtEnd() && currentToken().type == type;
    }

    protected Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    protected Token previous() {
        if(current == 0) return null;
        return tokens.get(current - 1);
    }

    public BlockStatement parseAsBlock() {
        BlockStatement statement = new BlockStatement();
        while (!isAtEnd()) {
            statement.addStatement(parseStatement());
        }
        return statement;
    }

    private BlockStatement parseBlockStatement() {
        BlockStatement block = new BlockStatement();
        consume(SyntaxToken.LBRACE, "Expected '{' to start block");
        while (!check(SyntaxToken.RBRACE) && !isAtEnd()) {
            block.addStatement(parseStatement());
        }
        consume(SyntaxToken.RBRACE, "Expected '}' to end block");
        return block;
    }

    private ASTStatement parseStatement() {
        if (check(SyntaxToken.LET) || check(SyntaxToken.VAR)) {
            return parseVariableDeclaration(advance());
        }

        if (match(SyntaxToken.IF)) return parseIfStatement(); // basic support for if-statement

        if(check(SyntaxToken.LBRACE)) {
            return parseBlockStatement();

        }

        if(match(SyntaxToken.FOR)) {
            return parseForStatement();
        }

        if(match(SyntaxToken.WHILE)) {
            return parseWhileStatement();
        }

        return parseExpressionStatement();
    }

    private ASTStatement parseWhileStatement() {
        consume(SyntaxToken.LPAREN, "Expected '(' after 'while'");
        ASTExpression condition = parseExpression();
        consume(SyntaxToken.RPAREN, "Expected ')' after while expression");
        ASTStatement doBranch = parseStatement(); // could be a block or single statement
        return new WhileStatement(condition, doBranch);
    }

    private ASTStatement parseForStatement() {
        // Same as Python, for is like foreach so the default runtime include a range function same as Python
        consume(SyntaxToken.LPAREN, "Expected '(' after 'for'");
        ForeachExpression iterable = parseForeachExpression();
        consume(SyntaxToken.RPAREN, "Expected ')' after iterable");
        ASTStatement doBranch = parseStatement(); // could be a block or single statement
        return new ForStatement(iterable, doBranch); // assuming you have this class
    }

    private ForeachExpression parseForeachExpression() {
        consume(SyntaxToken.LET, "A foreach expression must start with 'let'.");
        String name = consume(SyntaxToken.IDENTIFIER, "Expected variable name").value;
        consume(SyntaxToken.COLON, "Expected ':' after variable name");
        ASTExpression iterable = parseExpression();
        return new ForeachExpression(name, iterable);
    }

    private ExpressionStatement parseExpressionStatement() {
        ASTExpression expr = parseExpression();
        consume(SyntaxToken.JUMP, "Expected line jump or ';' after expression");
        return new ExpressionStatement(expr);
    }

    private ASTStatement parseVariableDeclaration(Token type) {
        String name = consume(SyntaxToken.IDENTIFIER, "Expected variable name").value;
        consume(SyntaxToken.ASSIGN, "Expected '=' in variable declaration");
        ASTExpression initialValue = parseExpression();
        consume(SyntaxToken.JUMP, "Expected line jump or ';' at end of variable declaration");
        return new VariableDeclaration(name, initialValue, type.type);
    }

    private ASTExpression parseExpression() {
        // Start from lowest precedence logical expression
        return parseEqualityExpression();
    }

    // Handles == and !=
    private ASTExpression parseEqualityExpression() {
        ASTExpression expr = parseBinaryExpression();
        while (match(LogicToken.EQUAL, LogicToken.NOT_EQUAL)) {
            Token operator = previous();
            ASTExpression right = parseBinaryExpression();
            expr = new ComparisonExpression(expr, (LogicToken) operator.type, right); // You might want to create a LogicalToken type instead
        }
        return expr;
    }

    private ASTExpression parseUnaryExpression() {
        // Simple unary operator support (e.g., -value, !value)
        if (match(ArithmeticToken.MINUS, LogicToken.NOT_EQUAL)) {
            Token operator = previous();
            ASTExpression right = parseUnaryExpression();
            return new BinaryExpression(new LiteralExpression(0), (ArithmeticToken) operator.type, right); // Temporary hack
        }
        return parsePrimaryExpression();
    }

    private ASTExpression parsePrimaryExpression() {
        if (match(SyntaxToken.NUMBER)) {
            StringBuilder nbrstr = new StringBuilder();
            Object value = null;
            nbrstr.append(previous().value);
            if (match(SyntaxToken.DOT)) {
                nbrstr.append('.');
                nbrstr.append(consume(SyntaxToken.NUMBER, "Expected number after '.' to complete the Double or Float").value);
                if (match(SyntaxToken.IDENTIFIER, "f")) {
                    nbrstr.append(previous().value);
                    value = Float.valueOf(nbrstr.toString());
                } else {
                    value = Double.valueOf(nbrstr.toString());
                }
            } else if (match(SyntaxToken.IDENTIFIER, "L")) {
                value = Long.valueOf(nbrstr.toString());
            } else {
                value = Integer.valueOf(nbrstr.toString());
            }
            return new LiteralExpression(value);
        }

        if (match(SyntaxToken.STRING)) {
            return new LiteralExpression(previous().value);
        }

        if (match(SyntaxToken.LPAREN)) {
            // Support for grouped expressions like (a + b)
            ASTExpression expr = parseExpression();
            consume(SyntaxToken.RPAREN, "Expected ')' after expression");
            return expr;
        }

        if(match(SyntaxToken.BOOLEAN)) {
            return new LiteralExpression(previous().value);
        }

        if (check(SyntaxToken.IDENTIFIER)) {
            ASTExpression expr = null;
            expr = parseContextAccess(expr);
            return expr;
        }

        if(check(SyntaxToken.LBRACE)) {
            return new StatementExpression(parseBlockStatement());
        }

        throw new RuntimeException("Unexpected token: " + currentToken());
    }

    private ASTExpression parseContextAccess(ASTExpression expr) {
        String idName = consume(SyntaxToken.IDENTIFIER, "Expected identifier").value;

        if (check(SyntaxToken.LPAREN)) {
            expr = parseMethodCall(expr);
        } else {
            expr = new ContextAccessExpression(expr, idName);
        }

        if (match(SyntaxToken.DOT)) {
            return parseContextAccess(expr);
        }

        return expr;
    }

    protected ArgumentExpression parseArgumentExpression() {
        if (match(SyntaxToken.RPAREN)) {
            return new SequencedArgumentExpression(List.of());
        } else if (peekToken(2).type == SyntaxToken.ASSIGN) {
            Map<String, ASTExpression> arguments = new HashMap<>();
            do {
                arguments.put(consume(SyntaxToken.IDENTIFIER, "Expected argument name").value, parseExpression());
            } while (match(SyntaxToken.COMMA));
            return new StructuredArgumentExpression(arguments);
        } else {
            List<ASTExpression> arguments = new ArrayList<>();
            do {
                arguments.add(parseExpression());
            } while (match(SyntaxToken.COMMA));
            return new SequencedArgumentExpression(arguments);
        }
    }

    private ASTExpression parseMethodCall(ASTExpression expr) {
        String methodName = previous().value;
        consume(SyntaxToken.LPAREN, "Expected '(' after method name");
        ArgumentExpression arguments = parseArgumentExpression();
        consume(SyntaxToken.RPAREN, "Expected ')' after method arguments");
        return new MethodCallExpression(expr, methodName, arguments);
    }

    private ASTExpression parseBinaryExpression() {
        ASTExpression expr = parseUnaryExpression();

        while (match(ArithmeticToken.PLUS, ArithmeticToken.MINUS, ArithmeticToken.MULT, ArithmeticToken.DIVIDE)) {
            Token operatorToken = previous();
            if (!(operatorToken.type instanceof ArithmeticToken)) {
                throw new RuntimeException("Unexpected token type: " + operatorToken.type);
            }
            ASTExpression right = parseUnaryExpression();
            expr = new BinaryExpression(expr, (ArithmeticToken) operatorToken.type, right);
        }

        return expr;
    }

    private ASTStatement parseIfStatement() {
        // Very basic "if" statement support (without else, yet)
        consume(SyntaxToken.LPAREN, "Expected '(' after 'if'");
        ASTExpression condition = parseExpression();
        consume(SyntaxToken.RPAREN, "Expected ')' after condition");

        ASTStatement thenBranch = parseStatement(); // could be a block or single statement
        return new IfStatement(condition, thenBranch); // assuming you have this class
    }
}
