# Sync Model

## Authority
The **Server (Desktop App)** is the single source of truth (Authority).
- **Play/Pause State**: Broadcast to all clients. Clients must obey.
- **Timeline**: The server broadcasts its current video timestamp.

## Client Behavior
1. **Passive Listener**: Browsers do not send play/seek commands (in MVP).
2. **Audio Stream**: Clients play a continuous HTTP stream.
3. **Drift**:
    - Currently, limited drift compensation.
    - The audio stream is "live" via built-in buffering.
    - Latency is determined by HTTP buffering (~1-3s).
    - Future versions will use `AudioContext` and explicit timestamp synchronization.

## Protocol via WebSocket
- `STATE_UPDATE`: `{ isPlaying: boolean, timestamp: long }`
- **On Join**: Client receives current state.
- **On Event**: Client receives update.
