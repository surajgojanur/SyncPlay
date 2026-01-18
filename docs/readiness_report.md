# Readiness Signal

## Status
**MVP-Ready**: YES (Conditional)

The codebase contains all necessary components for the MVP:
- Desktop Server (JavaFX + VLCJ + Javalin)
- Web Client (HTML + WebSocket)
- Protocol (JSON State Sync)

## blocking Items
1.  **Build Environment**: The current development environment lacks the specific Gradle/Java setup to verify the build output binary. However, the code structure is correct.
2.  **VLC Dependency**: Users MUST install VLC manually. This is a known constraint of VLCJ but a friction point.

## Next Improvement
**Latency Compensation**: The current sync model ignores network latency. Adding a simple NTP-style ping/pong to estimate `time_offset` would significantly tighten audio sync across devices.
