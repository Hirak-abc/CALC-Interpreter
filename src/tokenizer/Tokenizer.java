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

            if (Character.isDigit(current)) {
                tokens.add(readNumber());
            } else if (Character.isLetter(current)) {
                tokens.add(readIdentifier());
            } else if (current == '+') {
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
            } else if (current == '\n') {
                tokens.add(new Token(TokenType.NEWLINE, "\\n", line));
                line++;
                pos++;
            } else if (Character.isWhitespace(current)) {
                pos++;
            } else {
                pos++; // skip unknown for now
            }
        }

        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    private Token readNumber() {
        int start = pos;

        while (pos < source.length() && Character.isDigit(source.charAt(pos))) {
            pos++;
        }

        String value = source.substring(start, pos);
        return new Token(TokenType.NUMBER, value, line);
    }

    private Token readIdentifier() {
        int start = pos;

        while (pos < source.length() && Character.isLetterOrDigit(source.charAt(pos))) {
            pos++;
        }

        String value = source.substring(start, pos);
        return new Token(TokenType.IDENTIFIER, value, line);
    }
}
