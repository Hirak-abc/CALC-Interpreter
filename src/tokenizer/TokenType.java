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

    NEWLINE,
    EOF
}
