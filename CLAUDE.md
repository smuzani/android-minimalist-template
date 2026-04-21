# Claude Code Guidelines

## Project Overview

Android minimalist template focused on networking, Compose, and Flow. The architecture emphasizes **peripheral vision** — keeping related code close together so a reader (human or AI) can see everything at once. Dense, related code is preferred over clean-code abstractions for their own sake.

**Stack:** Retrofit + Gson, Coil, Jetpack Compose + Material 3, StateFlow, Hilt, Parcelize.

## Project-Specific Conventions

- **Hilt everywhere.** Repositories and services are provided by Hilt modules — no hand-constructed singletons inside ViewModels. See [randomuser/network/NetworkModule.kt](randomuser/src/main/java/com/template/randomuser/network/NetworkModule.kt) as the template.
- **Flow pattern.** Repositories expose cold `Flow<T>`; ViewModels expose hot `StateFlow<T>` via `.stateIn(...)`; Composables use `collectAsStateWithLifecycle()`. No bare `.collect {}` inside ViewModels. See [docs/flow-compose-patterns.md](docs/flow-compose-patterns.md).
- **`@Parcelize` for nav args.** Types that cross a navigation boundary are `@Parcelize data class`.
- **`Nerve` is a pragmatic hack, not an idiom.** [spine/Nerve.kt](spine/src/main/java/com/template/spine/Nerve.kt) is a mutable shared-state holder bridging scaffold ↔ screens. Use it only when a single outer `Scaffold` wraps many screens. For new features, prefer moving `Scaffold` inside each screen or hoisting an `AppBarState` the screens set via `LaunchedEffect`.
- **Shared modules need ≥ 2 consumers.** Don't put code in `designSystem/` or `spine/` unless multiple modules use it.
- **Package root:** `com.template.{module}`. Sub-packages by responsibility (`network/`, `screen/`, `theme/`).

## Coding Rules

Things that default LLM behavior tends to get wrong:

- **Don't extract helper functions** unless reused in ≥ 2 places or the original block is so opaque you'd need comments to explain it. A few inline lines beats a premature helper.
- **No comments** except for critical caveats (a hidden constraint, a workaround, a non-obvious invariant). Well-named identifiers carry the intent.
- **Name functions with existing domain vocabulary.** Don't invent a parallel term when one is already in use.

## Tooling Gates (MUST pass)

```bash
./gradlew ktlintFormat    # Reformat
./gradlew ktlintCheck     # Verify formatting
./gradlew build           # Compile, lint, check
```

Style (4-space indent, 140-col limit, no wildcard imports) is enforced by [`.editorconfig`](.editorconfig) + ktlint. Don't hand-tweak formatting.

## Architecture

```
app (MainActivity, MyApp, TemplateNavHost)
├── designSystem  (theme: colors, typography, shapes)
├── spine         (Nerve — see note above)
└── randomuser    (sample feature; replace when starting a real project)
```

## Git

Don't refer to Claude or Anthropic in commit messages.
