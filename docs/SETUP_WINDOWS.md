# Setup Guide for Windows

Follow these steps to build and run SyncPlay from source.

## Step 1: Clone the Repository
Open PowerShell or Command Prompt and run:
```powershell
git clone https://github.com/surajgojanur/SyncPlay.git
cd SyncPlay
```

## Step 2: Verify Installations
Ensure you have the prerequisites installed (see `PREREQUISITES.md`).
```powershell
java -version
# Should say "17" or higher

where vlc
# Should show path to vlc.exe
```

## Step 3: Build the Application
We use Gradle to build the app. You don't need to install Gradle manually; use the included wrapper (`gradlew`).

Run this command in the project folder:
```powershell
.\gradlew clean build
```
*Note: The first time you run this, it may take 1-2 minutes to download dependencies.*

## Step 4: Run the Application
Once the build finishes successfully, run the app:
```powershell
.\gradlew run
```

## Troubleshooting

### "Gradle is not recognized"
Make sure you are typing `.\gradlew` (with the dot-slash), not just `gradlew`.

### "Could not find valid VLC installation"
- Reinstall VLC from [VideoLAN.org](https://www.videolan.org/).
- Ensure you installed the **64-bit** version.
- Restart your computer if VLC was just installed.

### "Java version mismatch"
If you see errors complaining about class file versions (e.g., class file has wrong version 61.0), it means you are compiling with a newer Java but running with an older one, or vice-versa. Ensure `java -version` says 17.
