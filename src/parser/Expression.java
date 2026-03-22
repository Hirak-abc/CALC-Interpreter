package parser;

/**
 * Expression interface — every node in the expression tree implements this.
 *
 * The evaluate() method walks the tree bottom-up.
 * Returns:
 *   - a Double  for numeric results  (e.g. 10, 3.14, x + y * 2)
 *   - a String  for text results     (e.g. "Sitare", "Hello from CALC")
 *   - a Boolean for comparison results (e.g. score > 50)
 */
public interface Expression {

    /**
     * Evaluate this expression node using the current variable store.
     *
     * @param env  the Environment holding all currently defined variables
     * @return     a Double, String, or Boolean depending on what this node represents
     */
    Object evaluate(Environment env);
}
