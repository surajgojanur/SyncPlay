# Manual Test Plan

## 1. Happy Path
1. **Start** the application (`./gradlew run`).
2. **Load** a video file (`.mp4`).
3. **Verify** "Server running on 8080" status.
4. **Play** the video.
5. **Connect** phone/browser to `http://<ip>:8080`.
6. **Hear** audio on device.

## 2. Port Conflict
1. **Start** another service on port 8080 (e.g., `python3 -m http.server 8080`).
2. **Run** SyncPlay.
3. **Verify** status label shows "Server Error: ... Port 8080 in use?".
4. **Verify** app does not crash.

## 3. Missing VLC
1. **Rename** VLC executable or uninstall temporarily (if possible).
2. **Run** SyncPlay.
3. **Verify** status label shows "VLC Init Error: ...".
4. **Verify** app does not crash.
