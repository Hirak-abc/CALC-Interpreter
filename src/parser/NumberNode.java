package parser;

/**
 * NumberNode — represents a numeric literal in CALC source code.
 *
 * Examples from CALC syntax:
 *   x := 10       →  NumberNode(10.0)
 *   y := 3        →  NumberNode(3.0)
 *   @ 4 =>        →  NumberNode(4.0)   (the repeat count)
 */
public class NumberNode implements Expression {

    // The literal numeric value. Stored as double to support decimals.
    private final double value;

    /**
     * @param value  the numeric literal read from the source file
     */
    public NumberNode(double value) {
        this.value = value;
    }

    /**
     * Evaluating a number literal just returns the stored value.
     * No variable lookup needed — it is a constant.
     */
    @Override
    public Object evaluate(Environment env) {
        return value;
    }

    // ── optional: useful for debugging ──────────────────────────────────────
    @Override
    public String toString() {
        return "NumberNode(" + value + ")";
    }
}
