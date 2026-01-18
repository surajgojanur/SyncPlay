# System Architecture

SyncPlay is designed to be a lightweight, local-only streaming solution.

## Core Design Principles
1. **Single Port (8080)**: The web server and WebSocket server share the same port for simplicity.
2. **Local Processing**: Video decoding happens on the PC; only audio data is sent over the network.
3. **Low Latency**: Priority is given to immediate audio delivery over buffering.

## Data Flow

### 1. Desktop Video Flow
```
[Video File] -> [VLCJ Wrapper] -> [JavaFX UI] -> [Display]
```
- The video plays locally on your computer.
- JavaFX handles the window and buttons.
- VLCJ handles the heavy lifting of video decoding.

### 2. LAN Audio Flow
```
[VLCJ Audio Callback] -> [PCM Data Extraction] -> [WebSocket Server] -> [Wi-Fi] -> [Phone Browser]
```
- We intercept the raw audio (PCM format) from VLC.
- It is immediately sent to the internal WebSocket server.
- Connected clients (phones) receive these chunks in real-time.

## Design Decisions

### Why not HTTP Streaming?
HTTP streaming (like HLS or DASH) introduces significant buffers (usually 3-10 seconds) to ensure smooth playback over the internet.
**For local sync, this is too slow.** You would hear the audio seconds after the lips move.

### Why WebSocket Audio?
WebSockets allow for a persistent, bi-directional connection. We can send raw audio snippets as binary messages instantly.
- **Pros**: Extremely low latency (usually <50ms on LAN).
- **Cons**: Can be sensitive to poor Wi-Fi signals (jitter).
