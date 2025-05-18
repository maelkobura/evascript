package dev.kobura.evascript.lexer.token;

public enum SyntaxToken implements TokenType {
    // Keywords used in the language
    LET, VAR, RETURN, IF, ELSE, FOR, WHILE, SWITCH, CASE, CONTINUE, BREAK, FUNC, ASYNC, TRY, CATCH, FINALLY, DO, DEFAULT, NULL, UNDEFINED, THIS, CONST,

    // Represents variable names and other identifiers
    IDENTIFIER,

    // Punctuation and delimiters used in the syntax
    LPAREN, RPAREN, LBRACE, RBRACE, LBRACKET, RBRACKET, COMMA, COLON, SEMICOLON, DOT, BANG, TRIPLEDOT,

    // Data types and literals
    ASSIGN, NUMBER, DOUBLE, LONG, FLOAT, STRING, BOOLEAN, JUMP,
}
