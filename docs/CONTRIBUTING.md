# Contributing Guide

Thank you for your interest in improving SyncPlay!

## Core Development Rules

### 1. Java Version Lock
- We strictly use **Java 17**.
- Do not introduce features from Java 18+ as it breaks compatibility with some user environments.

### 2. Gradle Wrapper
- ALWAYS use `./gradlew` (Linux/Mac) or `.\gradlew` (Windows) to build.
- Do not rely on your locally installed Gradle version.

### 3. Coding Style
- Keep methods short and focused.
- Add comments for any complex logic, especially in `LocalServer.java`.
- Variable names should be descriptive (`clientSocket` vs `s`).

## Critical Areas (Be Careful)
Some parts of the codebase are fragile. **Do not change these casually:**
- **WebSocket Protocol**: changing the binary message format will break all existing clients.
- **VLCJ Options**: The arguments passed to VLC are tuned for low latency. Changing buffers or caching values can ruin synchronization.

## How to Propose Changes
1. Fork the repository.
2. Create a branch (`feature/my-cool-idea`).
3. Make your changes.
4. Test on **Windows** (primary target) if possible.
5. Submit a Pull Request.
