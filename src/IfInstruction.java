package src;

import java.util.List;

public class IfInstruction implements Instruction {

    private Expression condition;        // e.g. BinaryOpNode(score > 50)
    private List<Instruction> body;      // instructions inside the if block

    public IfInstruction(Expression condition, List<Instruction> body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute(Environment env) {
        // Step 1: Evaluate the condition → returns true or false
        Object result = condition.evaluate(env);

        // Step 2: If true, run all body instructions
        if (Boolean.TRUE.equals(result)) {
            for (Instruction instr : body) {
                instr.execute(env);
            }
        }
        // If false → skip body, do nothing
    }
}