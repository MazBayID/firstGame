# Architecture

## Runtime layers

- **Presentation:** Jetpack Compose screens and editor interactions.
- **Game domain:** chapter, mission, reward, and progression definitions.
- **Execution adapter:** Kotlin calls the embedded Python module through Chaquopy.
- **Mission runner:** Python validates a restricted AST, executes with limited built-ins, and exposes only the virtual `access.log`.
- **Content:** JSON story and mission definitions remain separate from Android UI code.

## First vertical slice

The Chapter 1 screen displays a fictional security case, a log excerpt, a Python editor, shortcut keys, and a run action. The Python exercise is executed by CPython through Chaquopy. Mission success is based on stdout matching the expected answer.

## Safety boundary

The current runner is a prototype guardrail, not a hardened sandbox. AST validation and a line-event budget reduce accidental misuse but do not constitute a security boundary against a determined attacker. Never execute remote or hostile code in-process. Before advanced networking or arbitrary native code, move execution to a dedicated isolated runtime with strict CPU, memory, filesystem, and network controls.

## Planned modules

- Mission repository and schema validation
- Player progression and save storage
- Editor syntax highlighting and diagnostics
- Isolated Python execution service
- C++ compile/run lab using a separate toolchain
