# SyncPlay for Windows

**SyncPlay** is a lightweight desktop application that allows you to play local video files on your Windows computer while streaming the audio to other devices (like phones or laptops) over your local Wi-Fi network.

## What Problem Does It Solve?
watching movies with friends or family usually means:
- Everyone crowding around one screen.
- Using a splitter for wired headphones (messy).
- Playing sound through speakers (disturbs others/neighbors).

**SyncPlay** solves this by turning your phone into a wireless headphone receiver. You play the video on your PC, and everyone listens in perfect sync using their own phones and headphones. No internet required.

## Supported Operating Systems
- **Windows 10** (64-bit)
- **Windows 11** (64-bit)

## What It Does NOT Do
- ❌ It does **NOT** stream video to other devices (Audio only).
- ❌ It does **NOT** work over the Internet (Local Network / LAN only).
- ❌ It does **NOT** support streaming services like Netflix or YouTube (Local files only).

## High-Level Architecture

```
[Windows PC (Host)]                 [User Phones (Clients)]
+-------------------+               +----------------------+
|                   |   Wy-Fi       |                      |
|  Video Player     |   ((( b )))   |  Web Browser         |
|  (Displays Video) |      |        |  (Plays Audio)       |
|         |         |------+------->|                      |
|         v         |      |        +----------------------+
|  Local Web Server |      |
|  (Streams Audio)  |      |        +----------------------+
|                   |      |        |                      |
+-------------------+      +------->|  Web Browser         |
                                    |  (Plays Audio)       |
                                    +----------------------+
```
