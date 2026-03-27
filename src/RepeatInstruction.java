package src;

import java.util.List;

public class RepeatInstruction implements Instruction {

    private int count;                  // how many times to repeat
    private List<Instruction> body;     // instructions inside the loop

    public RepeatInstruction(int count, List<Instruction> body) {
        this.count = count;
        this.body = body;
    }

    @Override
    public void execute(Environment env) {
        // Outer loop: runs 'count' times
        for (int i = 0; i < count; i++) {
            // Inner loop: runs every instruction in body
            for (Instruction instr : body) {
                instr.execute(env);
            }
        }
    }
}