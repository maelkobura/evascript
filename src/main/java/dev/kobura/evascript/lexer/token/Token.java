package dev.kobura.evascript.lexer.token;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Token {
    public final TokenType type;
    public final String value;
    public final int line;
}
