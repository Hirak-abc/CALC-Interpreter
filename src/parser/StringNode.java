package parser;

/**
 * StringNode — represents a string literal in CALC source code.
 *
 * Examples from CALC syntax:
 *   name := "Sitare"         →  StringNode("Sitare")
 *   >> "Hello from CALC"     →  StringNode("Hello from CALC")
 *
 * The quotes themselves are stripped by the Tokenizer before this node
 * is created — so `value` holds only the inner text, not the quotes.
 */
public class StringNode implements Expression {

    // The text content, without surrounding quotes.
    private final String value;

    /**
     * @param value  the string content (quotes already stripped by Tokenizer)
     */
    public StringNode(String value) {
        this.value = value;
    }

    /**
     * Evaluating a string literal just returns the stored text.
     * No variable lookup needed — it is a constant.
     */
    @Override
    public Object evaluate(Environment env) {
        return value;
    }

    // ── optional: useful for debugging ──────────────────────────────────────
    @Override
    public String toString() {
        return "StringNode(\"" + value + "\")";
    }
}
