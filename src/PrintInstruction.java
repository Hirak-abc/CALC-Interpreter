

package src;

public class PrintInstruction implements Instruction {

    private Expression expr;  // what to print

    public PrintInstruction(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void execute(Environment env) {
        // Step 1: Evaluate the expression to get the value
        Object value = expr.evaluate(env);

        // Step 2: Print cleanly
        if (value instanceof Double d && d == Math.floor(d)) {
            // It's a whole number like 16.0 → print as 16
            System.out.println(d.intValue());
        } else {
            // It's a decimal like 3.14 or a String like "hello"
            System.out.println(value);
        }
    }
}