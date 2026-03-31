package src.Interpreter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import src.Interpreter.Interpreter.Environment;

public class Interpreter {

    public void run(String sourceCode) {

        // ── STEP 1: Tokenize ──────────────────────────────
        Tokenizer tokenizer = new Tokenizer(sourceCode);
        List<Token> tokens = tokenizer.tokenize();

        // ── STEP 2: Parse ─────────────────────────────────
        Parser parser = new Parser(tokens);
        List<Instruction> instructions = parser.parse();

        // ── STEP 3: Execute ───────────────────────────────
        Environment env = new Environment();
        for (Instruction instruction : instructions) {
            instruction.execute(env);
        }
    }

    // Entry point — reads .calc file and runs it
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java Interpreter <file.calc>");
            return;
        }

        // Read entire file into a String
        String source = new String(Files.readAllBytes(Paths.get(args[0])));

        // Run it
        new Interpreter().run(source);
    }
}