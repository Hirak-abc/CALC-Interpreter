# CALC Interpreter
### Advanced Object-Oriented Programming in Java
**Sitare University | Group Project | 12.5 marks**

---

## What is CALC?

CALC (Concise Algorithmic Language for Computation) is a small scripting language built entirely in pure Java. It uses math-style notation — symbols over words — and supports variables, arithmetic, conditionals, and loops.

```
x := 10
y := 3
result := x + y * 2
>> result

? result > 10 =>
    >> "big number"

@ 4 =>
    >> "hello"
```

---

## Project Structure

```
CALC-Interpreter/
│
├── src/
│   ├── tokenizer/
│   │   ├── TokenType.java       — enum of every token kind
│   │   ├── Token.java           — immutable token object
│   │   └── Tokenizer.java       — reads source, emits token list
│   │
│   ├── parser/
│   │   ├── Expression.java      — interface for all expression nodes
│   │   ├── NumberNode.java      — numeric literal node
│   │   ├── StringNode.java      — string literal node
│   │   ├── VariableNode.java    — variable reference node
│   │   ├── BinaryOpNode.java    — arithmetic and comparison node
│   │   └── Parser.java          — builds List<Instruction> from tokens
│   │
│   └── interpreter/
│       ├── Environment.java     — variable store (Map<String, Object>)
│       ├── Instruction.java     — interface for all instructions
│       ├── AssignInstruction.java
│       ├── PrintInstruction.java
│       ├── IfInstruction.java
│       ├── RepeatInstruction.java
│       └── Interpreter.java     — pipeline entry point
│
├── programs/
│   ├── program1.calc            — arithmetic and variables
│   ├── program2.calc            — string output
│   ├── program3.calc            — conditional
│   └── program4.calc            — loop
│
├── .gitignore
└── README.md
```

---

## How It Works

The interpreter runs as a 3-step pipeline:

```
Source Code (.calc file)
        │
        ▼
   [ Tokenizer ]     — breaks source into a flat list of tokens
        │
        ▼
   [   Parser  ]     — builds an expression tree + list of instructions
        │
        ▼
   [ Evaluator ]     — walks the tree, executes each instruction
        │
        ▼
     Output
```

---

## CALC Syntax

| Operation   | Syntax                  | Example              |
|-------------|-------------------------|----------------------|
| Assign      | `x := <expression>`     | `x := 10`            |
| Print       | `>> <expression>`       | `>> result`          |
| Conditional | `? <condition> =>`      | `? score > 50 =>`    |
| Loop        | `@ <count> =>`          | `@ 4 =>`             |

### Operators
| Type       | Symbols         |
|------------|-----------------|
| Arithmetic | `+` `-` `*` `/` |
| Comparison | `>` `<`         |

---

## Sample Programs

**Program 1 — Arithmetic**
```
x := 10
y := 3
result := x + y * 2
>> result
```
Output: `16`

**Program 2 — Strings**
```
name := "Sitare"
>> name
>> "Hello from CALC"
```
Output:
```
Sitare
Hello from CALC
```

**Program 3 — Conditional**
```
score := 85
? score > 50 =>
    >> "Pass"
```
Output: `Pass`

**Program 4 — Loop**
```
i := 1
@ 4 =>
    >> i
    i := i + 1
```
Output:
```
1
2
3
4
```

---

## How to Compile and Run

**Compile all files from the project root:**
```bash
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse -Filter "*.java" src | % { $_.FullName })```

**Run a `.calc` program:**
```bash
java -cp bin interpreter.Interpreter programs/program1.calc
```

---

## Team

| Member   | Responsibility                        |
|----------|---------------------------------------|
| Member 1 | Tokenizer (TokenType, Token, Tokenizer) |
| Member 2 | Parser (Expression tree, Parser)      |
| Member 3 | Interpreter (Instructions, Environment, Interpreter) |

---

## Branch Strategy

```
main
├── tokenizer    — Member 1's branch
├── parser       — Member 2's branch
└── interpreter  — Member 3's branch
```

Each member works on their own branch and merges into `main` via Pull Request after team review.
