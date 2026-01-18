# Debugging Guide

If things aren't working, use this guide to diagnose the problem.

## 1. Verify the App is Running
- Look at the console/terminal where you ran `.\gradlew run`.
- Do you see `Server started on port 8080`?
- If the app crashes immediately, check `java -version` (must be 17+).

## 2. Verify Port 8080
Sometimes the port might be blocked or used by another app.
Open PowerShell and run:
```powershell
netstat -an | findstr "8080"
```
You should see:
`TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING`

## 3. Check WebSocket Connection
If you can load the page on your phone but hear no audio:
1. Open the page on your Desktop browser (Chrome).
2. Right-click > **Inspect** (or press F12).
3. Go to the **Console** tab.
4. Refresh the page.
5. Look for `WebSocket connected` or errors like `WebSocket connection failed`.

## Common Errors

### "Address already in use: bind"
- **Cause**: Another instance of SyncPlay (or another app) is already using port 8080.
- **Fix**: Close all other java windows or terminal processes. Run `taskkill /f /im java.exe` if stuck.

### "No Audio on Phone"
- **Cause**: Browsers block auto-playing audio.
- **Fix**: You MUST tap the screen / click "Play" on the web page at least once to allow audio.

### Video Plays but App Crashes
- **Cause**: VLC mismatch.
- **Fix**: Ensure you have 64-bit VLC installed. 32-bit VLC will crash 64-bit Java.
