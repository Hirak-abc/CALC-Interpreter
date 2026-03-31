package parser;

/**
 * BinaryOpNode — represents an operation between two expressions.
 *
 * Examples from CALC syntax:
 *   x + y * 2      →  BinaryOpNode( VariableNode("x"), "+", BinaryOpNode(VariableNode("y"), "*", NumberNode(2)) )
 *   score > 50     →  BinaryOpNode( VariableNode("score"), ">", NumberNode(50) )
 *
 * The tree shape handles operator precedence automatically —
 * the Parser builds it so that * and / sit deeper than + and -.
 * This node does not need to worry about precedence at all;
 * it just evaluates left, evaluates right, then applies the operator.
 *
 * Return types:
 *   Arithmetic operators  (+, -, *, /)  →  Double
 *   Comparison operators  (>, <)        →  Boolean
 */
public class BinaryOpNode implements Expression {

    private final Expression left;      // left-hand side expression
    private final String operator;      // "+", "-", "*", "/", ">", "<"
    private final Expression right;     // right-hand side expression

    /**
     * @param left      the left-hand side expression node
     * @param operator  the operator symbol as a String
     * @param right     the right-hand side expression node
     */
    public BinaryOpNode(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    /**
     * Evaluate both sides first, then apply the operator.
     *
     * Step 1: evaluate left  → gives a Double (numbers/variables always resolve to Double)
     * Step 2: evaluate right → gives a Double
     * Step 3: apply operator → returns Double (arithmetic) or Boolean (comparison)
     */
    @Override
    public Object evaluate(Environment env) {

        // Step 1 & 2: evaluate both sides and cast to double.
        // Both sides of any binary op in CALC must be numeric at runtime.
        double leftVal  = (Double) left.evaluate(env);
        double rightVal = (Double) right.evaluate(env);

        // Step 3: apply the operator and return the result.
        switch (operator) {

            // ── Arithmetic — return a Double ──────────────────────────────
            case "+": return leftVal + rightVal;
            case "-": return leftVal - rightVal;
            case "*": return leftVal * rightVal;
            case "/":
                if (rightVal == 0) {
                    throw new RuntimeException("Division by zero.");
                }
                return leftVal / rightVal;

            // ── Comparison — return a Boolean ─────────────────────────────
            case ">": return leftVal > rightVal;
            case "<": return leftVal < rightVal;

            // ── Unknown operator — should never happen if Parser is correct
            default:
                throw new RuntimeException("Unknown operator: " + operator);
        }
    }

    // ── optional: useful for debugging ──────────────────────────────────────
    @Override
    public String toString() {
        return "BinaryOpNode(" + left + " " + operator + " " + right + ")";
    }
}
