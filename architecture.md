# Architecture

## Overview
SyncPlay-LAN follows a client-server architecture where the desktop application acts as the Server and controls playback, while browsers act as Clients.

## Components

### 1. Desktop Application (Server)
- **UI Layer**: JavaFX for the video player interface, file selection, and server controls.
- **Media Engine**: VLCJ (libVLC wrapper) handles the actual video playback and audio extraction/transcoding if necessary.
- **HTTP Server**: Javalin (Jetty) serves the web client assets (HTML/JS/CSS) and provides media streams.
- **WebSocket Server**: Handles real-time synchronization messages (Play, Pause, Seek, Timestamp updates).

### 2. Web Client
- **HTML5 Audio/Video**: Uses standard browser APIs to play the stream.
- **Sync Logic**: Connects via WebSocket to receive time updates and adjusts playback speed/position to match the server.

## Data Flow
1. User opens Video X on Desktop App.
2. Desktop App starts HTTP server on port 8080 (default).
3. Client connects to `http://<server-ip>:8080`.
4. Client requests media stream.
5. Desktop App broadcasts "Play at timestamp Ti" via WebSocket.
6. Client seeks to Ti + NetworkLatency and plays.
