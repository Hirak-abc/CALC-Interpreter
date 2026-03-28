package parser;

import java.util.ArrayList;
import java.util.List;

// These will be in the tokenizer package — Member 1's work.
// Once the tokenizer branch is merged, these imports will resolve.
import tokenizer.Token;
import tokenizer.TokenType;

// These will be in the interpreter package — Member 3's work.
import interpreter.AssignInstruction;
import interpreter.PrintInstruction;
import interpreter.IfInstruction;
import interpreter.RepeatInstruction;
import interpreter.Instruction;
import interpreter.Environment;

/**
 * Parser — reads the List<Token> from the Tokenizer and builds a List<Instruction>.
 *
 * CALC syntax reminder:
 *   x := 10              →  AssignInstruction
 *   >> x                 →  PrintInstruction
 *   ? score > 50 =>      →  IfInstruction
 *   @ 4 =>               →  RepeatInstruction
 *
 * The parse() method walks through the token list top-down.
 * For each line it looks at the first token to decide what kind
 * of instruction it is reading, then delegates to a helper method.
 *
 * Operator precedence is handled by the chain:
 *   parseExpression()  →  handles + and -   (lowest precedence)
 *       parseTerm()    →  handles * and /   (higher precedence)
 *           parsePrimary() →  handles a single number, string, or variable
 * Each level calls the level below it, so * and / always bind tighter than + and -.
 */
public class Parser {

    private final List<Token> tokens;   // full token list from Tokenizer
    private int current = 0;            // index of the token we are looking at right now

    /**
     * @param tokens  the token list produced by Tokenizer.tokenize()
     */
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  PUBLIC ENTRY POINT
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Walk through every token and build the complete list of Instructions.
     *
     * @return  a List<Instruction> ready for the Evaluator to execute
     */
    public List<Instruction> parse() {
        List<Instruction> instructions = new ArrayList<>();

        while (!isAtEnd()) {
            skipNewlines();             // skip blank lines between instructions
            if (isAtEnd()) break;

            Instruction instruction = parseInstruction();
            if (instruction != null) {
                instructions.add(instruction);
            }
        }

        return instructions;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  INSTRUCTION DISPATCHER
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Look at the current token and decide what kind of instruction to parse.
     *
     * CALC instruction starters:
     *   IDENTIFIER followed by ASSIGN (:=)  →  assignment
     *   PRINT (>>)                           →  print
     *   IF (?)                               →  if block
     *   LOOP (@)                             →  repeat block
     */
    private Instruction parseInstruction() {

        // Assignment:  x := 10 + y
        if (check(TokenType.IDENTIFIER) && checkNext(TokenType.ASSIGN)) {
            return parseAssign();
        }

        // Print:  >> result   or   >> "Hello from CALC"
        if (check(TokenType.PRINT)) {
            return parsePrint();
        }

        // If block:  ? score > 50 =>
        if (check(TokenType.IF)) {
            return parseIf();
        }

        // Repeat block:  @ 4 =>
        if (check(TokenType.LOOP)) {
            return parseRepeat();
        }

        // Unknown token — skip it and move on (avoids hard crash on blank lines etc.)
        advance();
        return null;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  INSTRUCTION PARSERS  (all filled in — Day 8)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Parse:  x := <expression>
     * Consumes: IDENTIFIER  ASSIGN  <expression>  NEWLINE
     *
     * Examples from CALC:
     *   x := 10
     *   result := x + y * 2
     *   i := i + 1
     */
    private Instruction parseAssign() {
        String name = advance().getValue();       // consume IDENTIFIER — the variable name
        advance();                                // consume ASSIGN (:=)
        Expression expr = parseExpression();      // build the expression tree for the RHS
        consumeNewline();
        return new AssignInstruction(name, expr);
    }

    /**
     * Parse:  >> <expression>
     * Consumes: PRINT  <expression>  NEWLINE
     */
    private Instruction parsePrint() {
        advance();                                // consume PRINT (>>)
        Expression expr = parseExpression();      // build the expression tree
        consumeNewline();
        return new PrintInstruction(expr);
    }

    /**
     * Parse:  ? <condition> =>
     *             <body instructions>
     * Consumes: IF  <expression>  ARROW  NEWLINE  <indented body>
     */
    private Instruction parseIf() {
        advance();                                     // consume IF (?)
        Expression condition = parseExpression();      // parse the condition e.g. score > 50
        consume(TokenType.ARROW);                      // consume =>
        consumeNewline();
        List<Instruction> body = parseBlock();         // parse the indented body
        return new IfInstruction(condition, body);
    }

    /**
     * Parse:  @ <count> =>
     *             <body instructions>
     * Consumes: LOOP  NUMBER  ARROW  NEWLINE  <indented body>
     */
    private Instruction parseRepeat() {
        advance();                                     // consume LOOP (@)
        int count = ((Double) parsePrimary()
                        .evaluate(new Environment())).intValue(); // parse the repeat count
        consume(TokenType.ARROW);                      // consume =>
        consumeNewline();
        List<Instruction> body = parseBlock();         // parse the indented body
        return new RepeatInstruction(count, body);
    }

    /**
     * parseBlock — collects instructions that belong to the body of an if or repeat.
     *
     * In CALC the body is simply the indented lines that follow the => on the next line(s).
     * We keep reading instructions until we hit EOF or a token that starts a new
     * top-level instruction (IDENTIFIER+ASSIGN, PRINT, IF, LOOP) at the base level.
     *
     * Strategy used here: read exactly one instruction per indented line.
     * The Tokenizer marks indented lines — we collect them until indentation ends.
     *
     * Simple approach that works for the 4 sample programs:
     * read instructions while the next non-newline token is NOT a top-level starter
     * that sits at column 0, i.e. while it is indented.
     * Since our Tokenizer emits an INDENT token for indented lines, we check for that.
     * If the Tokenizer does not emit INDENT, we collect just one instruction (enough
     * for the given sample programs which all have single-line bodies).
     */
    private List<Instruction> parseBlock() {
        List<Instruction> body = new ArrayList<>();

        // Keep reading as long as the next line is indented (INDENT token present)
        // or — if the Tokenizer doesn't emit INDENT — read one instruction.
        skipNewlines();
        while (!isAtEnd()) {
            // If the Tokenizer emits an INDENT token, consume it and read one instruction
            if (check(TokenType.INDENT)) {
                advance();                            // consume INDENT
                Instruction instr = parseInstruction();
                if (instr != null) body.add(instr);
                skipNewlines();
            } else {
                // No INDENT token — fall back: read one instruction for the body
                // (covers simple single-line bodies in the sample programs)
                if (body.isEmpty()) {
                    Instruction instr = parseInstruction();
                    if (instr != null) body.add(instr);
                }
                break;
            }
        }

        return body;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  EXPRESSION PARSERS  (stubs — filled in Days 5, 6, 7)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Handles + and - (lowest precedence).
     * Calls parseTerm() for each operand so that * and / bind tighter.
     * Also handles comparison operators >, < at this level.
     *
     * Example:  x + y * 2
     *   parseTerm() gives x
     *   sees +
     *   parseTerm() gives the sub-tree BinaryOpNode(y, "*", 2)
     *   returns BinaryOpNode(x, "+", BinaryOpNode(y, "*", 2))
     *
     * Example:  score > 50
     *   parseTerm() gives VariableNode("score")
     *   sees >
     *   parseTerm() gives NumberNode(50)
     *   returns BinaryOpNode(VariableNode("score"), ">", NumberNode(50))
     *
     * The while loop handles chaining:  a + b - c
     *   → BinaryOpNode(BinaryOpNode(a, "+", b), "-", c)
     */
    private Expression parseExpression() {
        Expression left = parseTerm();      // get the left operand first

        // keep consuming +, -, >, < as long as they appear
        while (check(TokenType.PLUS) || check(TokenType.MINUS)
            || check(TokenType.GT)   || check(TokenType.LT)) {

            String operator = advance().getValue();     // consume the operator
            Expression right = parseTerm();             // get the right operand
            left = new BinaryOpNode(left, operator, right); // wrap into a node
        }

        return left;
    }

    /**
     * Handles * and / (higher precedence than + and -).
     * Calls parsePrimary() for each operand.
     *
     * Example:  y * 2
     *   parsePrimary() gives y
     *   sees *
     *   parsePrimary() gives 2
     *   returns BinaryOpNode(y, "*", 2)
     *
     * The while loop handles chaining:  a * b / c
     *   → BinaryOpNode(BinaryOpNode(a, "*", b), "/", c)
     */
    private Expression parseTerm() {
        Expression left = parsePrimary();   // get the left operand first

        // keep consuming * or / as long as they appear
        while (check(TokenType.MULTIPLY) || check(TokenType.DIVIDE)) {
            String operator = advance().getValue();     // consume * or /
            Expression right = parsePrimary();          // get the right operand
            left = new BinaryOpNode(left, operator, right); // wrap into a node
        }

        return left;
    }

    /**
     * Handles a single token: a number, a string, or a variable name.
     * This is the bottom of the precedence chain — it never calls back up.
     *
     * Examples:
     *   10       →  NumberNode(10.0)
     *   "Sitare" →  StringNode("Sitare")
     *   x        →  VariableNode("x")
     */
    private Expression parsePrimary() {

        // NUMBER — e.g. 10, 3, 4
        if (check(TokenType.NUMBER)) {
            Token t = advance();                          // consume the number token
            double value = Double.parseDouble(t.getValue());
            return new NumberNode(value);
        }

        // STRING — e.g. "Sitare", "Hello from CALC"
        // The Tokenizer has already stripped the surrounding quotes,
        // so t.getValue() is just the inner text.
        if (check(TokenType.STRING)) {
            Token t = advance();                          // consume the string token
            return new StringNode(t.getValue());
        }

        // IDENTIFIER — e.g. x, score, result
        if (check(TokenType.IDENTIFIER)) {
            Token t = advance();                          // consume the identifier token
            return new VariableNode(t.getName());
        }

        // Nothing matched — the source code has something unexpected here.
        throw new RuntimeException(
            "Expected a number, string, or variable but got '" +
            peek().getValue() + "' on line " + peek().getLine()
        );
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UTILITY HELPERS
    // ════════════════════════════════════════════════════════════════════════

    /** Returns the current token without consuming it. */
    private Token peek() {
        return tokens.get(current);
    }

    /** Returns the token after the current one, without consuming either. */
    private Token peekNext() {
        if (current + 1 >= tokens.size()) return tokens.get(tokens.size() - 1);
        return tokens.get(current + 1);
    }

    /** Consumes the current token and moves forward. Returns the consumed token. */
    private Token advance() {
        if (!isAtEnd()) current++;
        return tokens.get(current - 1);
    }

    /** Returns true if the current token matches the given type (without consuming). */
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    /** Returns true if the NEXT token (one ahead) matches the given type. */
    private boolean checkNext(TokenType type) {
        if (current + 1 >= tokens.size()) return false;
        return peekNext().getType() == type;
    }

    /** Consumes the current token if it matches; throws RuntimeException if it doesn't. */
    private Token consume(TokenType type) {
        if (check(type)) return advance();
        throw new RuntimeException(
            "Expected " + type + " but got '" + peek().getValue() +
            "' on line " + peek().getLine()
        );
    }

    /** Skips over any NEWLINE tokens. */
    private void skipNewlines() {
        while (check(TokenType.NEWLINE)) advance();
    }

    /** Consumes a NEWLINE if present; does nothing if already at EOF or next instruction. */
    private void consumeNewline() {
        if (check(TokenType.NEWLINE)) advance();
    }

    /** Returns true when we have reached the EOF token. */
    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }
}
