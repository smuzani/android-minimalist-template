# Claude Code Guidelines adopted from 

## Project Overview

This is an Android minimalist template project focused on networking, Compose, and Flow. The architecture emphasizes "peripheral vision" - keeping related code close together for better AI and human readability.

**Stack:**
- **Network:** Retrofit (API), Coil (images)
- **UI:** Jetpack Compose, Material 3
- **Logic:** Kotlin Flow (reactive programming), Hilt (DI)
- **Tests:** JUnit (unit), Maestro (UI tests)

**Philosophy:** The codebase follows a minimalist approach where everything should improve "peripheral vision" - the ability to see related code without navigating through multiple files. Dense, related code is preferred over clean code abstractions.

## Implementation Best Practices

### 0 — Purpose  

These rules ensure maintainability, safety, and developer velocity. 
**MUST** rules are enforced by CI; **SHOULD** rules are strongly recommended.

---

### 1 — Before Coding

- **BP-1 (MUST)** Ask the user clarifying questions.
- **BP-2 (SHOULD)** Draft and confirm an approach for complex work.  
- **BP-3 (SHOULD)** If ≥ 2 approaches exist, list clear pros and cons.

---

### 2 — While Coding

- **C-1 (SHOULD)** Follow TDD: scaffold stub -> write failing test -> implement.
- **C-2 (MUST)** Name functions with existing domain vocabulary for consistency.  
- **C-3 (SHOULD NOT)** Introduce classes when small testable functions suffice.  
- **C-4 (SHOULD)** Prefer simple, composable, testable functions.
- **C-5 (SHOULD)** Use explicit imports and avoid star imports.
- **C-6 (SHOULD NOT)** Add comments except for critical caveats; rely on self‑explanatory code.
- **C-7 (SHOULD)** Default to `data class`; use regular `class` only when inheritance or complex behavior is required. 
- **C-8 (SHOULD NOT)** Extract a new function unless it will be reused elsewhere, is the only way to unit-test otherwise untestable logic, or drastically improves readability of an opaque block.

---

### 3 — Testing

- **T-1 (MUST)** For simple functions, colocate unit tests in same module's `test/` directory.
- **T-2 (MUST)** For any API change, add/extend integration tests.
- **T-3 (MUST)** ALWAYS separate pure-logic unit tests from Android framework tests.
- **T-4 (SHOULD)** Prefer integration tests over heavy mocking.  
- **T-5 (SHOULD)** Unit-test complex algorithms thoroughly.
- **T-6 (SHOULD)** Test the entire structure in one assertion if possible
  ```kotlin
  assertThat(result).isEqualTo(listOf(value)) // Good

  assertThat(result).hasSize(1)               // Bad
  assertThat(result[0]).isEqualTo(value)      // Bad
  ```

---

### 4 — Android Architecture

- **A-1 (MUST)** Use StateFlow/Flow for reactive state management, not LiveData.  
- **A-2 (SHOULD)** Keep ViewModels focused on UI state transformation, delegate business logic to use cases or repositories.
- **A-3 (MUST)** Use Hilt for dependency injection consistently across all modules.
- **A-4 (SHOULD)** Place shared UI components in `designSystem` module only if used by ≥ 2 feature modules.

---

### 5 — Code Organization

- **O-1 (MUST)** Place code in shared modules only if used by ≥ 2 modules.
- **O-2 (SHOULD)** Keep related files in the same package for better "peripheral vision".
- **O-3 (MUST)** Follow package structure: `com.template.{module}.{feature}.{layer}`

---

### 6 — Tooling Gates

- **G-1 (MUST)** `./gradlew ktlintCheck` passes.  
- **G-2 (MUST)** `./gradlew build` passes (includes compile, test, lint).
- **G-3 (MUST)** `./gradlew test` passes.

---

### 7 - Git

- **GH-1 (SHOULD NOT)** Refer to Claude or Anthropic in commit messages.

---

## Writing Functions Best Practices

When evaluating whether a function you implemented is good or not, use this checklist:

1. Can you read the function and HONESTLY easily follow what it's doing? If yes, then stop here.
2. Does the function have high cyclomatic complexity? If it does, then it's probably sketchy.
3. Are there any common data structures and algorithms that would make this function much easier to follow and more robust?
4. Are there any unused parameters in the function?
5. Are there any unnecessary type casts that can be moved to function arguments?
6. Is the function easily testable without mocking Android framework components? If not, can this function be tested as part of an integration test?
7. Does it have any hidden untested dependencies or any values that can be factored out into the arguments instead?
8. Brainstorm 3 better function names and see if the current name is the best, consistent with rest of codebase.

IMPORTANT: you SHOULD NOT refactor out a separate function unless there is a compelling need, such as:
  - the refactored function is used in more than one place
  - the refactored function is easily unit testable while the original function is not AND you can't test it any other way
  - the original function is extremely hard to follow and you resort to putting comments everywhere just to explain it

## Writing Tests Best Practices

When evaluating whether a test you've implemented is good or not, use this checklist:

1. SHOULD parameterize inputs; never embed unexplained literals such as 42 or "test" directly in the test.
2. SHOULD NOT add a test unless it can fail for a real defect. Trivial asserts are forbidden.
3. SHOULD ensure the test description states exactly what the final assert verifies.
4. SHOULD compare results to independent, pre-computed expectations, never to the function's output re-used as the oracle.
5. SHOULD follow the same lint and style rules as prod code (ktlint, detekt).
6. SHOULD express invariants or axioms (e.g., commutativity, idempotence, round-trip) rather than single hard-coded cases whenever practical.
7. Unit tests for a function should be grouped under `class FunctionNameTest`.
8. ALWAYS use strong assertions over weaker ones e.g. `assertThat(x).isEqualTo(1)` instead of `assertThat(x).isAtLeast(1)`.
9. SHOULD test edge cases, realistic input, unexpected input, and value boundaries.
10. SHOULD NOT test conditions that are caught by the type checker.

## Architecture

The project uses a multi-module architecture with these key modules:

### Core Modules
- **app/**: Main application module containing `MainActivity.kt` and navigation (`TemplateNavHost.kt`)
- **designSystem/**: Shared UI components, themes, colors, typography (`theme/` package)
- **randomuser/**: Sample feature module demonstrating API integration with randomuser.me
- **spine/**: Communication layer between screens and top-level scaffold using `Nerve.kt` class

### Key Components
- **Nerve.kt** (`spine/`): Handles communication between lower-level screens and top-level scaffold using StateFlow for reactive UI updates (title, bottom bar button text/actions, padding)
- **Navigation**: Centralized in `TemplateNavHost.kt` using Jetpack Navigation Compose
- **DI**: Hilt for dependency injection across all modules

## Development Commands

### Code Quality
```bash
./gradlew ktlintFormat            # Format code
```

## Module Dependencies

The dependency structure follows:
```
app
├── designSystem (UI, themes, colors)
├── randomuser (sample API module, replace as requested)
└── spine (inter-screen communication)
```

## Remember Shortcuts

Remember the following shortcuts which the user may invoke at any time.

### QNEW

When I type "qnew", this means:

```
Understand all BEST PRACTICES listed in CLAUDE.md.
Your code SHOULD ALWAYS follow these best practices.
```

### QPLAN
When I type "qplan", this means:
```
Analyze similar parts of the codebase and determine whether your plan:
- is consistent with rest of codebase
- introduces minimal changes
- reuses existing code
```

### QCODE

When I type "qcode", this means:

```
Implement your plan and make sure your new tests pass.
Always run tests to make sure you didn't break anything else.
Always run `./gradlew ktlintFormat` on the newly created files to ensure standard formatting.
Always run `./gradlew build` to make sure compilation, type checking and linting passes.
```

### QCHECK

When I type "qcheck", this means:

```
You are a SKEPTICAL senior Android engineer.
Perform this analysis for every MAJOR code change you introduced (skip minor changes):

1. CLAUDE.md checklist Writing Functions Best Practices.
2. CLAUDE.md checklist Writing Tests Best Practices.
3. CLAUDE.md checklist Implementation Best Practices.
```

### QCHECKF

When I type "qcheckf", this means:

```
You are a SKEPTICAL senior Android engineer.
Perform this analysis for every MAJOR function you added or edited (skip minor changes):

1. CLAUDE.md checklist Writing Functions Best Practices.
```

### QCHECKT

When I type "qcheckt", this means:

```
You are a SKEPTICAL senior Android engineer.
Perform this analysis for every MAJOR test you added or edited (skip minor changes):

1. CLAUDE.md checklist Writing Tests Best Practices.
```

### QUX

When I type "qux", this means:

```
Imagine you are a human UX tester of the feature you implemented. 
Output a comprehensive list of scenarios you would test, sorted by highest priority.
```

### QGIT

When I type "qgit", this means:

```
Add all changes to staging, create a commit, and push to remote.

Follow this checklist for writing your commit message:
- SHOULD NOT refer to Claude or Anthropic in the commit message.
```