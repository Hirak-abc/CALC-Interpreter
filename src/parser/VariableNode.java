package parser;

/**
 * VariableNode — represents a variable reference in CALC source code.
 *
 * Examples from CALC syntax:
 *   result := x + y * 2     →  VariableNode("x"),  VariableNode("y")
 *   >> result                →  VariableNode("result")
 *   ? score > 50 =>          →  VariableNode("score")
 *
 * Unlike NumberNode and StringNode, this node does NOT carry the value
 * itself. It carries only the variable's name. The actual value is
 * looked up in the Environment at runtime — which is why evaluate()
 * needs the `env` parameter.
 */
public class VariableNode implements Expression {

    // The variable name as it appears in the source code.
    private final String name;

    /**
     * @param name  the variable name (e.g. "x", "score", "result")
     */
    public VariableNode(String name) {
        this.name = name;
    }

    /**
     * Look up the variable's current value in the Environment and return it.
     *
     * If the variable has never been assigned, Environment.get() will throw
     * a RuntimeException — that is intentional and correct behaviour.
     */
    @Override
    public Object evaluate(Environment env) {
        return env.get(name);   // delegates to Environment — may throw if undefined
    }

    // ── getter: Parser may need this to read the variable name ──────────────
    public String getName() {
        return name;
    }

    // ── optional: useful for debugging ──────────────────────────────────────
    @Override
    public String toString() {
        return "VariableNode(" + name + ")";
    }
}
