package tokenizer;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    private String source;
    private int pos = 0;
    private int line = 1;

    public Tokenizer(String source) {
        this.source = source;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < source.length()) {
            char current = source.charAt(pos);

            // Multi-character tokens (check first)
            if (current == ':' && peek() == '=') {
                tokens.add(new Token(TokenType.ASSIGN, ":=", line));
                pos += 2;
            } else if (current == '>' && peek() == '>') {
                tokens.add(new Token(TokenType.PRINT, ">>", line));
                pos += 2;
            } else if (current == '=' && peek() == '>') {
                tokens.add(new Token(TokenType.ARROW, "=>", line));
                pos += 2;
            }

            // Numbers
            else if (Character.isDigit(current)) {
                tokens.add(readNumber());
            }

            // Identifiers
            else if (Character.isLetter(current)) {
                tokens.add(readIdentifier());
            }

            // Strings
            else if (current == '"') {
                tokens.add(readString());
            }

            // Operators
            else if (current == '+') {
                tokens.add(new Token(TokenType.PLUS, "+", line));
                pos++;
            } else if (current == '-') {
                tokens.add(new Token(TokenType.MINUS, "-", line));
                pos++;
            } else if (current == '*') {
                tokens.add(new Token(TokenType.MULTIPLY, "*", line));
                pos++;
            } else if (current == '/') {
                tokens.add(new Token(TokenType.DIVIDE, "/", line));
                pos++;
            }

            // Comparison
            else if (current == '>') {
                tokens.add(new Token(TokenType.GT, ">", line));
                pos++;
            } else if (current == '<') {
                tokens.add(new Token(TokenType.LT, "<", line));
                pos++;
            }

            // Control symbols
            else if (current == '?') {
                tokens.add(new Token(TokenType.IF, "?", line));
                pos++;
            } else if (current == '@') {
                tokens.add(new Token(TokenType.LOOP, "@", line));
                pos++;
            }

            // Newline
            else if (current == '\n') {
                tokens.add(new Token(TokenType.NEWLINE, "\\n", line));
                line++;
                pos++;
            }

            // Whitespace
            else if (Character.isWhitespace(current)) {
                pos++;
            }

            // Error
            else {
                throw new RuntimeException("Unexpected character: " + current + " at line " + line);
            }
        }

        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    private Token readNumber() {
        int start = pos;

        while (pos < source.length() &&
                (Character.isDigit(source.charAt(pos)) || source.charAt(pos) == '.')) {
            pos++;
        }

        String value = source.substring(start, pos);
        return new Token(TokenType.NUMBER, value, line);
    }

    private Token readIdentifier() {
        int start = pos;

        while (pos < source.length() &&
                Character.isLetterOrDigit(source.charAt(pos))) {
            pos++;
        }

        String value = source.substring(start, pos);
        return new Token(TokenType.IDENTIFIER, value, line);
    }

    private Token readString() {
        pos++; // skip opening "
        int start = pos;

        while (pos < source.length() && source.charAt(pos) != '"') {
            pos++;
        }

        String value = source.substring(start, pos);
        pos++; // skip closing "

        return new Token(TokenType.STRING, value, line);
    }

    private char peek() {
        if (pos + 1 >= source.length()) return '\0';
        return source.charAt(pos + 1);
    }
}
