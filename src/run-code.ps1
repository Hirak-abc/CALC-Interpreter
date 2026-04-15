# Build all CALC packages, then run the entry point that matches the file you clicked "Run Code" on.
# Used by VS Code / Cursor Code Runner (see .vscode/settings.json).
param(
    [Parameter(Mandatory = $false)]
    [string] $SourceFile = ""
)

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot
Set-Location $root

if (-not (Test-Path "tokenizer")) {
    $src = Join-Path $root "src"
    if (Test-Path (Join-Path $src "tokenizer")) {
        Set-Location $src
    }
}

$javac = "javac"
if (-not (Get-Command javac -ErrorAction SilentlyContinue)) {
    Write-Error "javac not found. Install a JDK and add it to PATH."
}

& $javac --release 17 -encoding UTF-8 `
    tokenizer\*.java `
    parser\*.java `
    interpreter\*.java

if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

$name = if ($SourceFile) { [System.IO.Path]::GetFileName($SourceFile) } else { "" }

switch -Wildcard ($name) {
    "Interpreter.java" {
        # If you pass a file path to the script, forward it to the interpreter.
        if ($SourceFile -and (Test-Path $SourceFile) -and ($SourceFile.ToLower().EndsWith(".calc"))) {
            java -cp . interpreter.Interpreter $SourceFile
        } else {
            java -cp . interpreter.Interpreter
        }
        exit $LASTEXITCODE
    }
    Default {
        Write-Host "Build succeeded. No Code Runner target for '$name'. Examples:"
        Write-Host "  java -cp . interpreter.Interpreter path\to\program.calc"
        Write-Host "  type path\to\program.calc | java -cp . interpreter.Interpreter"
    }
}
