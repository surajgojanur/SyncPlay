# SyncPlay Setup Guide

## Prerequisites
- **Java 17+**: Ensure `java -version` shows version 17 or higher.
- **VLC Media Player**: Must be installed.
    - Linux: `sudo apt install vlc` (or equivalent)
    - Windows: Install from videolan.org (Must match OS architecture, usually 64-bit).

## Building
1. Clone the repository.
2. Run `./gradlew build` to compile and run tests.

## Running
Run `./gradlew run`.

## Troubleshooting
### "Server Error: Port 8080 in use?"
The application tries to bind to port 8080. If another application is using this port, you will see a red error message.
**Fix**: Stop the other application or change the port in `SyncServer` (future config support planned).

### "VLC Init Error"
This means VLCJ could not find the native VLC libraries.
**Fix**:
- Ensure VLC is installed.
- On Windows, ensure you installed the 64-bit version if using 64-bit Java.
