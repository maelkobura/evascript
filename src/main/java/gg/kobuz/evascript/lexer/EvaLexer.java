package gg.kobuz.evascript.lexer;

import gg.kobuz.evascript.lexer.token.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaLexer {
    private final String input;
    private int position = 0;

    private static final Map<String, TokenType> keywords = new HashMap<>();

    static {
        keywords.put("let", SyntaxToken.LET);
        keywords.put("var", SyntaxToken.VAR);
        keywords.put("return", SyntaxToken.RETURN);
        keywords.put("if", SyntaxToken.IF);
        keywords.put("else", SyntaxToken.ELSE);
        keywords.put("for", SyntaxToken.FOR);
        keywords.put("while", SyntaxToken.WHILE);
        keywords.put("switch", SyntaxToken.SWITCH);
        keywords.put("case", SyntaxToken.CASE);
        keywords.put("or", LogicToken.OR);
        keywords.put("and", LogicToken.AND);
        keywords.put("not", LogicToken.NOT_EQUAL);
        keywords.put("break", SyntaxToken.BREAK);
        keywords.put("continue", SyntaxToken.CONTINUE);
    }

    public EvaLexer(String input) {
        this.input = input;
    }

    private char peek() {
        return position < input.length() ? input.charAt(position) : '\0';
    }

    private char peekNext() {
        return position + 1 < input.length() ? input.charAt(position + 1) : '\0';
    }

    private char advance() {
        char current = peek();
        position++;
        return current;
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(peek())) {
            advance();
        }
    }

    private Token readNumber() {
        StringBuilder number = new StringBuilder();
        TokenType type = SyntaxToken.NUMBER;
        while (Character.isDigit(peek())) {
            number.append(advance());
        }
        if(peek() == '.') {
            number.append(advance());
            while (Character.isDigit(peek())) {
                number.append(advance());
            }
            if(peek() == 'f') {
                number.append(advance());
                type = SyntaxToken.FLOAT;
            }else {
                type = SyntaxToken.DOUBLE;
            }
        } else if(peek() == 'L') {
            number.append(advance());
            type = SyntaxToken.LONG;
        }

        return new Token(type, number.toString());
    }

    private Token readStringLiteral() {
        StringBuilder value = new StringBuilder();
        if(advance() == '"') {
            while (peek() != '"') {
                value.append(advance());
            }
        }else {
            while (peek() != '\'') {
                value.append(advance());
            }
        }
        advance(); // Skip closing " or '
        return new Token(SyntaxToken.STRING, value.toString());
    }

    private Token readIdentifier() {
        StringBuilder identifier = new StringBuilder();
        while (Character.isLetterOrDigit(peek())) {
            identifier.append(advance());
        }
        String text = identifier.toString();
        if(text.equals("true") || text.equals("false")) return new Token(SyntaxToken.BOOLEAN, text);
        TokenType type = keywords.getOrDefault(text, SyntaxToken.IDENTIFIER);
        return new Token(type, text);
    }

    public Token readToken() {
        skipWhitespace();
        char current = peek();

        // 🔁 Gestion du saut de ligne et annulation par '|'
        if (current == '\n' || current == '\r') {
            boolean isWindows = false;

            if (current == '\r' && peekNext() == '\n') {
                isWindows = true;
            }

            // Vérifie le caractère précédent dans le texte brut
            if (position > 0 && input.charAt(position - 1) == '|') {
                // Consomme le saut de ligne
                advance(); // \r
                if (isWindows) advance(); // \n
                else if (current == '\n') advance(); // \n seul
                // Continue avec le token suivant
                return readToken();
            }

            // Sinon, retourne un token JUMP
            advance(); // \r
            if (isWindows) advance(); // \n
            else if (current == '\n') advance(); // \n seul
            return new Token(SyntaxToken.JUMP, "\\n");
        }

        if (Character.isDigit(current)) {
            return readNumber();
        } else if (Character.isLetter(current)) {
            return readIdentifier();
        } else if (current == '"' || current == '\'') {
            return readStringLiteral();
        } else {
            switch (current) {
                case '(': return new Token(SyntaxToken.LPAREN, String.valueOf(advance()));
                case ')': return new Token(SyntaxToken.RPAREN, String.valueOf(advance()));
                case '{': return new Token(SyntaxToken.LBRACE, String.valueOf(advance()));
                case '}': return new Token(SyntaxToken.RBRACE, String.valueOf(advance()));
                case '<': return peekNext() == '='
                        ? new Token(LogicToken.LESS_EQUAL, "" + advance() + advance())
                        : new Token(LogicToken.LESS, String.valueOf(advance()));
                case '>': return peekNext() == '='
                        ? new Token(LogicToken.GREATER_EQUAL, "" + advance() + advance())
                        : new Token(LogicToken.GREATER, String.valueOf(advance()));
                case ';': return new Token(SyntaxToken.JUMP, String.valueOf(advance()));
                case '.': return new Token(SyntaxToken.DOT, String.valueOf(advance()));
                case '[': return new Token(SyntaxToken.LBRACKET, String.valueOf(advance()));
                case ']': return new Token(SyntaxToken.RBRACKET, String.valueOf(advance()));
                case ',': return new Token(SyntaxToken.COMMA, String.valueOf(advance()));
                case '=': return new Token(SyntaxToken.ASSIGN, String.valueOf(advance()));
                case '+': return new Token(ArithmeticToken.PLUS, String.valueOf(advance()));
                case '-': return new Token(ArithmeticToken.MINUS, String.valueOf(advance()));
                case '*': return new Token(ArithmeticToken.MULT, String.valueOf(advance()));
                case '%': return new Token(ArithmeticToken.MODULO, String.valueOf(advance()));
                case '&': return new Token(LogicToken.AND, String.valueOf(advance()));
                case ':': return new Token(SyntaxToken.COLON, String.valueOf(advance()));
                case '/': return new Token(ArithmeticToken.DIVIDE, String.valueOf(advance()));
                default:
                    throw new RuntimeException("Unexpected character: " + current);
            }
        }
    }


    public List<Token> readAll() {
        List<Token> tokens = new ArrayList<>();
        while (position < input.length()) {
            tokens.add(readToken());
        }
        return tokens;
    }
}
