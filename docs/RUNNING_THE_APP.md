# Running SyncPlay

## Starting the Application
1. Double-click `gradlew.bat` OR run `.\gradlew run` in PowerShell.
2. The SyncPlay window should appear.

## Using the Interface
- **Load Video**: Click this button to browse for a video file (MP4, MKV, AVI).
- **Play/Pause**: Controls playback.
- **Stop**: Stops playback and resets the position.

## How to Stream Audio to Your Phone

### 1. Connect to Wi-Fi
Ensure your PC and your phone are connected to the **same Wi-Fi network**.

### 2. Find Your PC's IP Address
The application should display your local IP address in the console log, or you can find it manually:
- Open PowerShell.
- Type `ipconfig`.
- Look for **IPv4 Address** (e.g., `192.168.1.15`).

### 3. Open the Browser on Your Phone
On your phone (or any other device), open a web browser (Chrome, Safari, Firefox) and type:

`http://<YOUR-PC-IP>:8080`

**Example:**
If your IP is `192.168.1.15`, type:
`http://192.168.1.15:8080`

### 4. Start Listening
- You will see a "Join Audio" or "Play" button on your phone.
- Tap it.
- Now, when you play the video on your PC, the audio will come out of your phone!

## Tips for Best Performance
- Use **5GHz Wi-Fi** if possible for lower latency.
- Keep phones close to the router.
- Use wired headphones on your phone for zero additional delay.
