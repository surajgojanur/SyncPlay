# Prerequisites for Windows

Before running SyncPlay, you must ensure your system has the following software installed.

## 1. Java 17 (Required)
SyncPlay is built on Java. You need **Java 17** or newer to run it.
- **Why?** Older versions of Java do not support the libraries we use.
- **Verification:** Open PowerShell and type:
  ```powershell
  java -version
  ```
  You should see output similar to `openjdk version "17.0.x"`.

## 2. VLC Media Player (Required)
SyncPlay uses the VLC engine to decode and play video files. You must have the standard desktop version of VLC installed.
- **Why?** We rely on `vlcj`, which connects Java to VLC's native libraries.
- **Important:** Install the **64-bit version** of VLC if your Java is 64-bit (which it likely is).
- **Download:** [VideoLAN.org](https://www.videolan.org/vlc/download-windows.html)
- **Verification:** Open PowerShell and type:
  ```powershell
  where vlc
  ```
  It should return a path like `C:\Program Files\VideoLAN\VLC\vlc.exe`.

## Common Mistakes
- ⚠️ **Installing the Windows Store version of VLC**: This will NOT work. You must use the classic desktop installer (.exe or .msi).
- ⚠️ **Mismatched Architectures**: Installing 32-bit Java with 64-bit VLC (or vice versa) will cause crashes. Ensure both are 64-bit.
