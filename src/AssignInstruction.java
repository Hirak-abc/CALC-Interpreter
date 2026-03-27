package src;

import java.beans.Expression;

public class AssignInstruction implements Instruction  {
    private String name;       // variable name  e.g. "result"
    private Expression expr;   // right hand side e.g. BinaryOpNode(x + y*2)

    // Constructor — Parser calls this to create the instruction
    public AssignInstruction(String name, Expression expr) {
        this.name = name;
        this.expr = expr;
    }

    @Override
    public void execute(Environment env) {
        // Step 1: Calculate the right hand side
        Object value = expr.evaluate(env);
        
        // Step 2: Store the result in the variable store
        env.set(name, value);
}
}
