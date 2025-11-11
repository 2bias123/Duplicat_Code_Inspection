# Duplicate Code Inspection – JetBrains Internship Task

This project implements the `AstNodeExtensions.isEquivalentTo` method for comparing two AST subtrees while ignoring whitespace and comment tokens.

## Overview
- Compares both **leaf** and **composite** nodes recursively.
- Skips whitespace/comment nodes via helper functions.
- Uses `maxDepth` as a **recursion safety guard** (prevents stack overflow).
- Normalizes text for consistent leaf comparison.

Implementation files:
- `src/main/kotlin/ast/IAstNode.kt`
- `src/main/kotlin/ast/AstNodeExtensions.kt`

## Note on Language
The task requested C#, but I implemented it in **Kotlin** because I’m more experienced and efficient in it.  
The logic, structure, and algorithm are directly transferable to C# — only syntax differs.

## Tests
Unit tests in `src/test/kotlin/ast/AstNodeExtensionsTest.kt` cover:
- Basic equivalence and difference
- Ignoring whitespace/comments
- Structural mismatch detection
- Null/self-comparison
- Recursion safety with `maxDepth`

## Run
```bash
./gradlew test
```

## Limitations

This method checks **exact structural equivalence** only.  
Duplicate detection isn’t useful when:
- Code differs only by variable names or literals.
- The duplicate is intentional (e.g., small helpers).
- It appears in different contexts or generated code.

Handling those cases would need semantic or higher-level analysis, outside this prototype’s scope.
