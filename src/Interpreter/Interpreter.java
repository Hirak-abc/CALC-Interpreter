package interpreter;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.List;

import parser.Parser;
import tokenizer.Token;
import tokenizer.Tokenizer;

public class Interpreter {

     public void run(String sourceCode) {
        run(sourceCode, new Environment());
    }

    public void run(String sourceCode,Environment env) {

        // ── STEP 1: Tokenize ──────────────────────────────
        Tokenizer tokenizer = new Tokenizer(sourceCode);
        List<Token> tokens = tokenizer.tokenize();

        // ── STEP 2: Parse ─────────────────────────────────
        Parser parser = new Parser(tokens);
        List<Instruction> instructions = parser.parse();

        // ── STEP 3: Execute ───────────────────────────────
        for (Instruction instruction : instructions) {
            instruction.execute(env);
        }
    }

    // Entry point — reads .calc file and runs it
   public static void main(String[] args) throws IOException {
        if (args.length >= 1) {
            // Read entire file into a String
            String source = Files.readString(Paths.get(args[0]), StandardCharsets.UTF_8);

            // Run it
            new Interpreter().run(source);
            return;
        }

        // No file path was provided: ask whether to read from stdin or run example.calc.
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            System.out.print("Do you want to enter a CALC program in the terminal? (yes/no): ");
            String choice = br.readLine();
            boolean useStdin = choice != null && choice.trim().equalsIgnoreCase("yes");

            if (useStdin) {
                System.out.println("Enter your CALC program now. End input with EOF.");
                System.out.println("Windows EOF: Ctrl+Z then Enter");
                System.out.println("macOS/Linux EOF: Ctrl+D");

                StringBuilder program = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    program.append(line).append('\n');
                }

                String source = program.toString();
                if (source.isBlank()) {
                    System.out.println("No program provided. Exiting.");
                    return;
                }

                new Interpreter().run(source);
                return;
            }

            // If user says "no" (or anything else), run example.calc by default.
            // Try common locations depending on the working directory.
            String[] candidates = {
                    "example.calc",
                    "src\\example.calc",
                    "src/example.calc"
            };

            IOException last = null;
            for (String candidate : candidates) {
                try {
                    String source = Files.readString(Paths.get(candidate), StandardCharsets.UTF_8);
                    new Interpreter().run(source);
                    return;
                } catch (IOException e) {
                    last = e;
                }
            }

            System.out.println("Couldn't find example.calc to run automatically.");
            System.out.println("Tried: example.calc, src\\example.calc");
            System.out.println("Tip: run with an explicit path argument, e.g.:");
            System.out.println("  java interpreter.Interpreter path\\to\\example.calc");
            if (last != null) throw last;
        }
    }
}
