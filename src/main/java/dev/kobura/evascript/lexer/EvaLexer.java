package dev.kobura.evascript.lexer;

import dev.kobura.evascript.lexer.token.*;
import dev.kobura.evascript.lexer.token.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaLexer {
    private static final Map<String, TokenType> keywords = new HashMap<>();

    static {
        keywords.put("let", SyntaxToken.LET);
        keywords.put("func", SyntaxToken.FUNC);
        keywords.put("var", SyntaxToken.VAR);
        keywords.put("return", SyntaxToken.RETURN);
        keywords.put("if", SyntaxToken.IF);
        keywords.put("async", SyntaxToken.ASYNC);
        keywords.put("try", SyntaxToken.TRY);
        keywords.put("catch", SyntaxToken.CATCH);
        keywords.put("finally", SyntaxToken.FINALLY);
        keywords.put("else", SyntaxToken.ELSE);
        keywords.put("for", SyntaxToken.FOR);
        keywords.put("while", SyntaxToken.WHILE);
        keywords.put("switch", SyntaxToken.SWITCH);
        keywords.put("case", SyntaxToken.CASE);
        keywords.put("do", SyntaxToken.DO);
        keywords.put("or", LogicToken.OR);
        keywords.put("and", LogicToken.AND);
        keywords.put("not", LogicToken.NOT_EQUAL);
        keywords.put("break", SyntaxToken.BREAK);
        keywords.put("continue", SyntaxToken.CONTINUE);
        keywords.put("default", SyntaxToken.DEFAULT);
    }

    private final String input;
    private int position = 0;
    private int line = 1;

    public EvaLexer(String input) {
        this.input = input;
    }

    // Returns the current character without advancing
    private char peek() {
        return position < input.length() ? input.charAt(position) : '\0';
    }

    // Returns the next character without advancing
    private char peekNext() {
        return position + 1 < input.length() ? input.charAt(position + 1) : '\0';
    }

    // Returns the current character and advances
    private char advance() {
        char current = peek();
        position++;
        return current;
    }

    // Skips any whitespace characters
    private void skipWhitespace() {
        while (Character.isWhitespace(peek()) && peek() != '\n' && peek() != '\r') {
            advance();
        }
    }

    // Reads and returns a number literal
    private Token readNumber() {
        StringBuilder number = new StringBuilder();
        TokenType type = SyntaxToken.NUMBER;

        while (Character.isDigit(peek())) {
            number.append(advance());
        }

        if (peek() == '.') {
            number.append(advance());
            while (Character.isDigit(peek())) {
                number.append(advance());
            }
            if (peek() == 'f') {
                number.append(advance());
                type = SyntaxToken.FLOAT;
            } else {
                type = SyntaxToken.DOUBLE;
            }
        } else if (peek() == 'L') {
            number.append(advance());
            type = SyntaxToken.LONG;
        }

        return new Token(type, number.toString(), line);
    }

    // Reads and returns a string literal
    private Token readStringLiteral() {
        StringBuilder value = new StringBuilder();
        char quote = advance(); // consume opening quote

        while (peek() != quote && peek() != '\0') {
            value.append(advance());
        }

        advance(); // consume closing quote
        return new Token(SyntaxToken.STRING, value.toString(), line);
    }

    // Reads and returns an identifier or keyword
    private Token readIdentifier() {
        StringBuilder identifier = new StringBuilder();

        while (Character.isLetterOrDigit(peek())) {
            identifier.append(advance());
        }

        String text = identifier.toString();

        if (text.equals("true") || text.equals("false")) {
            return new Token(SyntaxToken.BOOLEAN, text, line);
        }

        TokenType type = keywords.getOrDefault(text, SyntaxToken.IDENTIFIER);
        return new Token(type, text, line);
    }
    
    

    // Reads and returns a single token
    public Token readToken() {
        skipWhitespace();
        char current = peek();

        // Handle newlines and continuation via '|'
        if (current == '\n' || current == '\r') {
            advance();
            line++;
            return null;
        }

        if (Character.isDigit(current)) {
            return readNumber();
        } else if (Character.isLetter(current)) {
            return readIdentifier();
        } else if (current == '"' || current == '\'') {
            return readStringLiteral();
        } else {
            switch (current) {
                case '(': return new Token(SyntaxToken.LPAREN, String.valueOf(advance()), line);
                case ')': return new Token(SyntaxToken.RPAREN, String.valueOf(advance()), line);
                case '{': return new Token(SyntaxToken.LBRACE, String.valueOf(advance()), line);
                case '}': return new Token(SyntaxToken.RBRACE, String.valueOf(advance()), line);
                case '<':
                    return peekNext() == '='
                            ? new Token(LogicToken.LESS_EQUAL, "" + advance() + advance(), line)
                            : new Token(LogicToken.LESS, String.valueOf(advance()), line);
                case '>':
                    return peekNext() == '='
                            ? new Token(LogicToken.GREATER_EQUAL, "" + advance() + advance(), line)
                            : new Token(LogicToken.GREATER, String.valueOf(advance()), line);
                case ';': return new Token(SyntaxToken.JUMP, String.valueOf(advance()), line);
                case '.':
                    return peekNext() == '.' && peekNext() == '.'
                            ? new Token(SyntaxToken.TRIPLEDOT, "" + advance() + advance() + advance(), line)
                            : new Token(SyntaxToken.DOT, String.valueOf(advance()), line);
                case '[': return new Token(SyntaxToken.LBRACKET, String.valueOf(advance()), line);
                case ']': return new Token(SyntaxToken.RBRACKET, String.valueOf(advance()), line);
                case ',': return new Token(SyntaxToken.COMMA, String.valueOf(advance()), line);
                case '=': return peekNext() == '='
                        ? new Token(LogicToken.EQUAL, "" + advance() + advance(), line)
                        : new Token(SyntaxToken.ASSIGN, String.valueOf(advance()), line);
                case '+':
                    return peekNext() == '+'
                            ? new Token(ArithmeticToken.ADD, "" + advance() + advance(), line)
                            : new Token(ArithmeticToken.PLUS, String.valueOf(advance()), line);
                case '-':
                    return peekNext() == '-'
                            ? new Token(ArithmeticToken.SUB, "" + advance() + advance(), line)
                            : new Token(ArithmeticToken.MINUS, String.valueOf(advance()), line);
                case '*': return new Token(ArithmeticToken.MULT, String.valueOf(advance()), line);
                case '%': return new Token(ArithmeticToken.MODULO, String.valueOf(advance()), line);
                case '&': return new Token(LogicToken.AND, String.valueOf(advance()), line);
                case ':': return new Token(SyntaxToken.COLON, String.valueOf(advance()), line);
                case '/': return new Token(ArithmeticToken.DIVIDE, String.valueOf(advance()), line);
                case '!': return new Token(SyntaxToken.BANG, String.valueOf(advance()), line);
                case '@': return new Token(SyntaxToken.AT, String.valueOf(advance()), line);
                default:
                    throw new RuntimeException("Unexpected character: " + current + " at line " + line);
            }
        }
    }

    // Reads and returns all tokens from the input
    public List<Token> readAll() {
        List<Token> tokens = new ArrayList<>();
        while (position < input.length()) {
            Token token = readToken();
            if(token != null) {
                tokens.add(token);
            }
        }
        return tokens;
    }
}
