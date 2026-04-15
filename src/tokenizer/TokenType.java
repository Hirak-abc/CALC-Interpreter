package tokenizer;

public enum TokenType {
    NUMBER,
    STRING,
    IDENTIFIER,

    ASSIGN,     // :=
    PRINT,      // >>
    IF,         // ?
    LOOP,       // @
    ARROW,      // =>

    PLUS,       // +
    MINUS,      // -
    MULTIPLY,   // *
    DIVIDE,     // /

    GT,         // >
    LT,         // <

    INDENT,     // leading spaces on a line (optional; body parsing)
    NEWLINE,
    EOF
}
