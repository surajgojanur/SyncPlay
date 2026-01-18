# SyncPlay Protocol Design

## Overview
The system uses a **Hybrid HTTP + WebSocket** approach for minimal latency and simplicity.
- **HTTP**: Transports static assets (HTML/JS) and the audio stream (progressive download).
- **WebSocket**: Handles real-time synchronization, state updates, and latency estimation.

---

## 1. Endpoints (Server)

| Method | Path       | Description                                      | Content-Type         |
|:-------|:-----------|:-------------------------------------------------|:---------------------|
| `GET`  | `/`        | Serves the client Web UI                         | `text/html`          |
| `GET`  | `/stream`  | Audio stream (Progressive Download)              | `audio/mpeg`, `audio/wav` |
| `GET`  | `/static/*`| Static assets (CSS, JS)                          | `text/css`, `script/js`|
| `WS`   | `/ws`      | WebSocket endpoint for Sync & Control            | `Upgrade: websocket` |

---

## 2. Audio Streaming Strategy
- **Format**: MP3 or WAV (raw PCM if bandwidth allows).
- **Control**: The browser's `<audio>` element handles buffering and decoding.
- **Access**: Clients access `/stream`. The server pipes the current audio playback to this endpoint.
  - *Note*: Ideally, the stream is "live". If the user joins late, they receive the stream from the current point (or slightly buffered).

---

## 3. WebSocket Messages (JSON)

### Server -> Client

#### `SYNC`
Broadcast periodically (e.g., every 1-5 seconds) or on state change (Pause/Play).
```json
{
  "type": "SYNC",
  "payload": {
    "isPlaying": boolean,
    "positionMillis": number,  // Current playback position of Server
    "serverTxTime": number     // Server system time (epoch ms) when sent
  }
}
```

#### `METADATA`
Sent when a new track loads or on connection.
```json
{
  "type": "METADATA",
  "payload": {
    "filename": string,
    "durationMillis": number
  }
}
```

#### `PONG`
Response to a client's PING.
```json
{
  "type": "PONG",
  "payload": {
    "clientTxTime": number,    // Original timestamp from client
    "serverRxTime": number     // Server system time when PING received
  }
}
```

### Client -> Server

#### `PING`
Sent by client initially to calculate RTT (Round Trip Time).
```json
{
  "type": "PING",
  "payload": {
    "clientTxTime": number     // Client system time (epoch ms)
  }
}
```

---

## 4. Synchronization Logic (Client-Side)

1.  **Clock Offset Calculation**: (Optional for simple version)
    - Client sends `PING`.
    - Server responds `PONG`.
    - `RTT = now() - clientTxTime`.
    - `Latency = RTT / 2`.
    - *Simpler Approach*: Just assume logical stream position matches server, ignore clock diffs, focus on playback position.

2.  **Playback Adjustment**:
    - On receiving `SYNC`:
        - Calculate `estimatedServerPosition = msg.positionMillis + (now() - msg.serverTxTime)`.
        - Compare with `clientPlayer.currentTime`.
        - **Threshold**: If drift > `50ms` (or user defined):
            - **Hard Sync**: `clientPlayer.currentTime = estimatedServerPosition`.
            - **Soft Sync**: Speed up/slow down playback rate (e.g., 1.05x / 0.95x) to catch up gently.
