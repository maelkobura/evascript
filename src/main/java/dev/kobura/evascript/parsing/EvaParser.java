package dev.kobura.evascript.parsing;

import dev.kobura.evascript.lexer.token.*;
import dev.kobura.evascript.parsing.ast.expression.context.*;
import dev.kobura.evascript.parsing.ast.statement.*;
import dev.kobura.evascript.lexer.token.*;
import dev.kobura.evascript.parsing.ast.ASTExpression;
import dev.kobura.evascript.parsing.ast.ASTStatement;
import dev.kobura.evascript.parsing.ast.FunctionDeclaration;
import dev.kobura.evascript.parsing.ast.expression.ForeachExpression;
import dev.kobura.evascript.parsing.ast.expression.LambdaExpression;
import dev.kobura.evascript.parsing.ast.expression.LiteralExpression;
import dev.kobura.evascript.parsing.ast.expression.context.variable.VariableDeclaration;
import dev.kobura.evascript.parsing.ast.expression.context.variable.args.ArgumentDeclarationExpression;
import dev.kobura.evascript.parsing.ast.expression.context.variable.args.ArgumentExpression;
import dev.kobura.evascript.parsing.ast.expression.context.variable.args.SequencedArgumentExpression;
import dev.kobura.evascript.parsing.ast.expression.context.variable.args.StructuredArgumentExpression;
import dev.kobura.evascript.parsing.ast.expression.data.DataExpression;
import dev.kobura.evascript.parsing.ast.expression.data.DataItemExpression;
import dev.kobura.evascript.parsing.ast.expression.data.DataKeyExpression;
import dev.kobura.evascript.parsing.ast.expression.data.RestExpression;
import dev.kobura.evascript.parsing.ast.expression.data.array.ArrayAccessExpression;
import dev.kobura.evascript.parsing.ast.expression.data.array.ArrayExpression;
import dev.kobura.evascript.parsing.ast.expression.logical.BinaryExpression;
import dev.kobura.evascript.parsing.ast.expression.logical.ComparisonExpression;
import dev.kobura.evascript.parsing.ast.expression.logical.UnaryExpression;
import dev.kobura.evascript.parsing.ast.expression.tokenable.NullExpression;
import dev.kobura.evascript.parsing.ast.expression.tokenable.ThisExpression;
import dev.kobura.evascript.parsing.ast.expression.tokenable.UndefinedExpression;
import dev.kobura.evascript.parsing.ast.statement.*;
import dev.kobura.evascript.parsing.ast.statement.loop.BreakStatement;
import dev.kobura.evascript.parsing.ast.statement.loop.ContinueStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (current == 0) return null;
        return tokens.get(current - 1);
    }

    public BlockStatement parseAsBlock() {
        BlockStatement statement = new BlockStatement();
        while (!isAtEnd()) {
            ASTStatement parsedStatement = parseStatement();
            if(parsedStatement != null) {
                statement.addStatement(parsedStatement);
            }
        }
        return statement;
    }

    public ExpressionStatement parseAsExpression() {
        return parseExpressionStatement();
    }

    private BlockStatement parseBlockStatement() {
        BlockStatement block = new BlockStatement();
        consume(SyntaxToken.LBRACE, "Expected '{' to start block");
        while (!check(SyntaxToken.RBRACE) && !isAtEnd()) {
            ASTStatement parsedStatement = parseStatement();
            if(parsedStatement != null) {
                block.addStatement(parsedStatement);
            }
        }
        consume(SyntaxToken.RBRACE, "Expected '}' to end block");
        return block;
    }

    private ASTStatement parseStatement() {
        if (check(SyntaxToken.LET) || check(SyntaxToken.VAR)) {
            return parseVariableDeclaration(advance());
        }

        if (match(SyntaxToken.IF)) return parseIfStatement(); // basic support for if-statement

        if (check(SyntaxToken.LBRACE)) {
            return parseBlockStatement();
        }

        if (match(SyntaxToken.FOR)) {
            return parseForStatement();
        }

        if (match(SyntaxToken.WHILE)) {
            return parseWhileStatement();
        }

        if (match(SyntaxToken.FUNC)) {
            return parseFunctionDeclaration();
        }

        if (match(SyntaxToken.TRY)) {
            return parseTryCatchFinally();
        }

        if (match(SyntaxToken.BREAK)) {
            return new BreakStatement();
        }

        if (match(SyntaxToken.CONTINUE)) {
            return new ContinueStatement();
        }

        if (match(SyntaxToken.DO)) {
            return parseDoWhileStatement();
        }

        if (match(SyntaxToken.SWITCH)) {
            return parseSwitchStatement();
        }

        if (match(SyntaxToken.JUMP)) {
            return null;
        }

        if(match(SyntaxToken.RETURN)) {
            return new ReturnStatement(parseExpression());
        }

        return parseExpressionStatement();
    }

    private ASTStatement parseSwitchStatement() {
        consume(SyntaxToken.LPAREN, "Expected '(' after 'switch'");
        ASTExpression condition = parseExpression();
        consume(SyntaxToken.RPAREN, "Expected ')' after switch expression");
        consume(SyntaxToken.LBRACE, "Expected '{' after switch expression");
        List<SwitchCase> cases = new ArrayList<>();

        while (!match(SyntaxToken.RBRACE) && !isAtEnd()) {
            if (match(SyntaxToken.DEFAULT)) {
                consume(SyntaxToken.COLON, "Expected ':' after default case");
                List<ASTStatement> statements = new ArrayList<>();
                while (!check(SyntaxToken.CASE) && !check(SyntaxToken.DEFAULT) && !match(SyntaxToken.RBRACE)) {
                    ASTStatement parsedStatement = parseStatement();
                    if(parsedStatement != null) {
                        statements.add(parsedStatement);
                    }
                }
                cases.add(new SwitchCase(null, true, statements));
            } else {
                consume(SyntaxToken.CASE, "Expected 'case' after switch expression");
                ;
                ASTExpression expression = parseExpression();
                consume(SyntaxToken.COLON, "Expected ':' after case expression");
                List<ASTStatement> statements = new ArrayList<>();

                while (!check(SyntaxToken.CASE) && !check(SyntaxToken.DEFAULT) && !match(SyntaxToken.RBRACE)) {
                    ASTStatement parsedStatement = parseStatement();
                    if(parsedStatement != null) {
                        statements.add(parsedStatement);
                    }
                }
                cases.add(new SwitchCase(expression, false, statements));
            }
        }
        if (match(SyntaxToken.DEFAULT)) {
            consume(SyntaxToken.COLON, "Expected ':' after default case");
            List<ASTStatement> statements = List.of();
            while (!match(SyntaxToken.CASE)) {
                statements.add(parseStatement());
            }
            cases.add(new SwitchCase(null, true, statements));
        }
        consume(SyntaxToken.RBRACE, "Expected '}' after switch expression");

        return new SwitchStatement(condition, cases);
    }

    private ASTStatement parseDoWhileStatement() {
        BlockStatement block = parseBlockStatement();
        consume(SyntaxToken.WHILE, "Expected 'while' after do block");
        ASTExpression condition = parseExpression();
        return new DoWhileStatement(block, condition);
    }


    private ASTStatement parseFunctionDeclaration() {
        boolean async = false;
        if (match(SyntaxToken.ASYNC)) {
            async = true;
        }
        String name = consume(SyntaxToken.IDENTIFIER, "Expected function name").value;
        consume(SyntaxToken.LPAREN, "Expected '(' after function name");
        ArgumentDeclarationExpression expression = parseArgumentDeclarationExpression();
        consume(SyntaxToken.RPAREN, "Expected ')' after arguments");
        BlockStatement body = parseBlockStatement();
        return new FunctionDeclaration(name, expression, body, async);
    }

    private ASTStatement parseTryCatchFinally() {
        BlockStatement tryBlock = parseBlockStatement();
        BlockStatement catchBlock = null;
        String catchExpression = null;
        BlockStatement finallyBlock = null;
        if (match(SyntaxToken.CATCH)) {
            consume(SyntaxToken.LPAREN, "Expected '(' after 'catch'");
            catchExpression = consume(SyntaxToken.IDENTIFIER, "Expected exception name.").value;
            consume(SyntaxToken.RPAREN, "Expected ')' after catch expression");
            catchBlock = parseBlockStatement();
        }
        if (match(SyntaxToken.FINALLY)) {
            finallyBlock = parseBlockStatement();
        }
        if (catchBlock == null && finallyBlock == null) {
            throw new RuntimeException("Expected either 'catch' or 'finally'");
        }
        return new TryStatement(tryBlock, catchBlock, catchExpression, finallyBlock);
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
        if (match(SyntaxToken.ASSIGN)) {
            return new ExpressionStatement(new AssignementExpression(expr, parseExpression()));
        }
        consume(SyntaxToken.JUMP, "Expected line jump or ';' after expression");
        return new ExpressionStatement(expr);
    }

    private ASTStatement parseVariableDeclaration(Token type) {
        Token nameToken = consume(SyntaxToken.IDENTIFIER, "Expected variable name");
        if(!match(SyntaxToken.ASSIGN)) {
            return new VariableDeclaration(nameToken.value, null, (SyntaxToken) type.type, nameToken);
        }
        ASTExpression initialValue = parseExpression();
        consume(SyntaxToken.JUMP, "Expected line jump or ';' at end of variable declaration");
        return new VariableDeclaration(nameToken.value, initialValue, (SyntaxToken) type.type, nameToken);
    }

    private ASTExpression parseExpression() {
        // Start from lowest precedence logical expression
        return parseEqualityExpression();
    }

    private ASTExpression parseDataBlock() {
        consume(SyntaxToken.LBRACE, "Expected '{' to start data block");
        List<ASTExpression> items = new ArrayList<>();
        while (!check(SyntaxToken.RBRACE) && !isAtEnd()) {
            if (match(SyntaxToken.TRIPLEDOT)) {
                items.add(new RestExpression(parseExpression()));
            } else {
                items.add(parseDataItem());
            }

            if (!check(SyntaxToken.RBRACE)) {
                consume(SyntaxToken.COMMA, "Expected ',' between data items");
            } else {
                break;
            }

        }
        consume(SyntaxToken.RBRACE, "Expected '}' to end data block");
        return new DataExpression(items);
    }

    private DataItemExpression parseDataItem() {
        ASTExpression key;
        if (match(SyntaxToken.LBRACKET)) {
            key = parseExpression();
            consume(SyntaxToken.RBRACKET, "Expected ']' to end data key");
        } else if (match(SyntaxToken.IDENTIFIER) || match(SyntaxToken.NUMBER)) {
            key = new DataKeyExpression(previous());
        } else {
            throw new RuntimeException("Unexpected key type: " + currentToken());
        }

        consume(SyntaxToken.COLON, "Expected ':' after data key");
        ASTExpression value = parseExpression();

        return new DataItemExpression(key, value);
    }

    // Handles == and !=
    private ASTExpression parseEqualityExpression() {
        ASTExpression expr = parseBinaryExpression();
        while (match(LogicToken.EQUAL, LogicToken.NOT_EQUAL, LogicToken.LESS, LogicToken.GREATER, LogicToken.LESS_EQUAL, LogicToken.GREATER_EQUAL)) {
            Token operator = previous();
            ASTExpression right = parseBinaryExpression();
            expr = new ComparisonExpression(expr, operator, right);
        }
        return expr;
    }

    private ASTExpression parseUnaryExpression() {
        Token operator = previous();
        ASTExpression right = parsePrimaryExpression();
        return new UnaryExpression(operator, right);
    }

    private ASTExpression parsePrimaryExpression() {
        if (match(ArithmeticToken.MINUS, SyntaxToken.BANG)) {
            return parseUnaryExpression();
        }
        if (match(SyntaxToken.NUMBER)) {
            Token numberToken = previous();
            StringBuilder nbrstr = new StringBuilder();
            Object value = null;
            nbrstr.append(numberToken.value);
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
            return new LiteralExpression(value, numberToken);
        }

        if(match(SyntaxToken.DOUBLE)) {
            Token doubleToken = previous();
            return new LiteralExpression(Double.valueOf(doubleToken.value), doubleToken);
        }

        if (match(SyntaxToken.STRING)) {
            Token stringToken = previous();
            return new LiteralExpression(stringToken.value, stringToken);
        }

        if (match(SyntaxToken.LPAREN)) {
            // Support for grouped expressions like (a + b)
            ASTExpression expr = parseExpression();
            consume(SyntaxToken.RPAREN, "Expected ')' after expression");
            return expr;
        }

        if (match(SyntaxToken.BOOLEAN)) {
            Token booleanToken = previous();
            return new LiteralExpression(Boolean.valueOf(booleanToken.value), booleanToken);
        }

        if (check(SyntaxToken.IDENTIFIER)) {
            ASTExpression expr = null;
            expr = parseContextAccess(expr);
            if (check(SyntaxToken.LBRACKET)) {
                expr = parseArrayAccess(expr);
            }
            return expr;
        }

        if (check(SyntaxToken.FUNC)) {
            return parseLambdaExpression();
        }

        if (check(SyntaxToken.FUNC)) {
            return parseLambdaExpression();
        }

        if(match(SyntaxToken.AT)) {
            return parseSystemCallExpression();
        }

        if (check(SyntaxToken.LBRACE)) {
            return parseDataBlock();
        }

        if (check(SyntaxToken.LBRACKET)) {
            ASTExpression expr = null;
            expr = parseArrayDeclaration();
            if (check(SyntaxToken.LBRACKET)) {
                expr = parseArrayAccess(expr);
            }
            return expr;
        }

        if(check(SyntaxToken.NULL)) {
            return new NullExpression();
        }

        if(check(SyntaxToken.UNDEFINED)) {
            return new UndefinedExpression();
        }

        if(check(SyntaxToken.THIS)) {
            return new ThisExpression();
        }

        if(check(SyntaxToken.ELSE)) {
            throw new RuntimeException("Unexpected else body: " + currentToken());
        }

        throw new RuntimeException("Unexpected token: " + currentToken());
    }

    private ASTExpression parseLambdaExpression() {
        consume(SyntaxToken.FUNC, "Expected 'func' for declare lambda expression");
        boolean async = false;
        if(match(SyntaxToken.ASYNC)) {
            async = true;
        }
        consume(SyntaxToken.LPAREN, "Expected '(' before lambda arguments");
        ArgumentDeclarationExpression arguments = parseArgumentDeclarationExpression();
        consume(SyntaxToken.RPAREN, "Expected ')' after lambda arguments");
        BlockStatement body = parseBlockStatement();
        return new LambdaExpression(arguments, body, async);
    }

    private ASTExpression parseArrayAccess(ASTExpression expr) {
        consume(SyntaxToken.LBRACKET, "Expected '[' to start array access");
        ASTExpression index = parseExpression();
        consume(SyntaxToken.RBRACKET, "Expected ']' to end array access");
        return new ArrayAccessExpression(expr, index);
    }

    private ASTExpression parseArrayDeclaration() {
        consume(SyntaxToken.LBRACKET, "Expected '[' to start array declaration");
        List<ASTExpression> items = new ArrayList<>();
        while (!check(SyntaxToken.RBRACKET)) {
            items.add(parseExpression());
            if (!check(SyntaxToken.RBRACKET)) {
                consume(SyntaxToken.COMMA, "Expected ',' between array items");
            } else {
                break;
            }
        }
        consume(SyntaxToken.RBRACKET, "Expected ']' to end array declaration");
        return new ArrayExpression(items);
    }

    private ASTExpression parseContextAccess(ASTExpression expr) {
        Token identifierToken = consume(SyntaxToken.IDENTIFIER, "Expected identifier");

        if (check(SyntaxToken.LPAREN)) {
            expr = parseMethodCall(expr);
        } else {
            expr = new ContextAccessExpression(expr, identifierToken.value, identifierToken);
        }

        if (match(SyntaxToken.DOT)) {
            return parseContextAccess(expr);
        }

        return expr;
    }

    protected ArgumentExpression parseArgumentExpression() {
        if (check(SyntaxToken.RPAREN)) {
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

    protected ArgumentDeclarationExpression parseArgumentDeclarationExpression() {
        if (check(SyntaxToken.RPAREN)) {
            return new ArgumentDeclarationExpression(List.of());
        } else {
            List<String> arguments = new ArrayList<>();
            do {
                arguments.add(consume(SyntaxToken.IDENTIFIER, "Expected argument name").value);
            } while (match(SyntaxToken.COMMA));
            return new ArgumentDeclarationExpression(arguments);
        }
    }

    private ASTExpression parseMethodCall(ASTExpression expr) {
        Token methodToken = previous();
        String methodName = methodToken.value;
        consume(SyntaxToken.LPAREN, "Expected '(' after method name");
        ArgumentExpression arguments = parseArgumentExpression();
        consume(SyntaxToken.RPAREN, "Expected ')' after method arguments");
        return new MethodCallExpression(expr, methodName, arguments, methodToken);
    }

    private ASTExpression parseSystemCallExpression() {
        String methodName = consume(SyntaxToken.IDENTIFIER, "Expected method name").value;
        consume(SyntaxToken.LPAREN, "Expected '(' after method name");
        ArgumentExpression arguments = parseArgumentExpression();
        consume(SyntaxToken.RPAREN, "Expected ')' after method arguments");

        SystemCallExpression expr = new SystemCallExpression(methodName, arguments);

        if(match(SyntaxToken.DOT)) {
            return parseContextAccess(expr);
        }

        return expr;
    }

    private ASTExpression parseBinaryExpression() {
        ASTExpression expr = parsePrimaryExpression();

        while (match(ArithmeticToken.PLUS, ArithmeticToken.MINUS, ArithmeticToken.MULT, ArithmeticToken.DIVIDE, ArithmeticToken.MODULO)) {
            Token operatorToken = previous();
            if (!(operatorToken.type instanceof ArithmeticToken)) {
                throw new RuntimeException("Unexpected token type: " + operatorToken.type);
            }
            ASTExpression right = parsePrimaryExpression();
            expr = new BinaryExpression(expr, operatorToken, right);
        }
        if (match(ArithmeticToken.ADD, ArithmeticToken.SUB)) {
            Token operatorToken = previous();
            if (!(operatorToken.type instanceof ArithmeticToken)) {
                throw new RuntimeException("Unexpected token type: " + operatorToken.type);
            }
            expr = new UnaryExpression(operatorToken, expr);
        }

        return expr;
    }

    private ASTStatement parseIfStatement() {
        // Very basic "if" statement support (without else, yet)
        consume(SyntaxToken.LPAREN, "Expected '(' after 'if'");
        ASTExpression condition = parseExpression();
        consume(SyntaxToken.RPAREN, "Expected ')' after condition");

        ASTStatement thenBranch = parseStatement(); // could be a block or single statement
        ASTStatement elseBranch = null;
        if(match(SyntaxToken.ELSE)) {
            if(match(SyntaxToken.IF)) elseBranch = parseIfStatement();
            else elseBranch = parseStatement();
        }
        return new IfStatement(elseBranch, thenBranch, condition); // assuming you have this class
    }
}
