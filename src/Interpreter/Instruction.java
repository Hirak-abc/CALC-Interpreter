package src.Interpreter;

import src.Interpreter.Interpreter.Environment;

public interface Instruction {
    void execute(Environment env);
}
