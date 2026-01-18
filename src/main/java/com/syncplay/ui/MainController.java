package com.syncplay.ui;

import com.syncplay.server.LocalServer;
import com.syncplay.server.ServerService;
import com.syncplay.server.ServerStartupException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import java.io.File;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurface;
import uk.co.caprica.vlcj.player.base.callback.AudioCallback;
import com.sun.jna.Pointer;

public class MainController {

    @FXML
    private Label statusLabel;
    @FXML
    private Button playButton;
    @FXML
    private Button loadButton;
    @FXML
    private StackPane videoContainer;

    private ServerService serverService;
    private File currentFile;
    private MediaPlayerFactory mediaPlayerFactory;
    private EmbeddedMediaPlayer mediaPlayer;
    private ImageView videoImageView;

    public void initialize() {
        // Initialize Server
        serverService = ServerService.getInstance();
        new Thread(() -> {
            try {
                serverService.startServer();
                LocalServer server = serverService.getServer();
                if (server != null) {
                    Platform.runLater(() -> statusLabel.setText("Server running on 8080"));
                }
            } catch (ServerStartupException e) {
                Platform.runLater(() -> {
                    statusLabel.setText("Server Error: " + e.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red;");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("Unexpected Server Error: " + e.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red;");
                });
                e.printStackTrace();
            }
        }).start();

        playButton.setDisable(true);

        // Initialize VLCJ
        try {
            this.mediaPlayerFactory = new MediaPlayerFactory();
            this.mediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();

            this.videoImageView = new ImageView();
            videoImageView.fitWidthProperty().bind(videoContainer.widthProperty());
            videoImageView.fitHeightProperty().bind(videoContainer.heightProperty());
            videoImageView.setPreserveRatio(true);
            videoContainer.getChildren().add(videoImageView);

            mediaPlayer.videoSurface().set(new ImageViewVideoSurface(this.videoImageView));

            // Event listeners for state updates (Local player is source of truth)
            mediaPlayer.events()
                    .addMediaPlayerEventListener(new uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter() {
                        @Override
                        public void playing(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer) {
                            Platform.runLater(() -> {
                                playButton.setText("Pause");
                                broadcastState();
                            });
                        }

                        @Override
                        public void paused(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer) {
                            Platform.runLater(() -> {
                                playButton.setText("Play");
                                broadcastState();
                            });
                        }

                        @Override
                        public void stopped(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer) {
                            Platform.runLater(() -> {
                                playButton.setText("Play");
                                broadcastState();
                            });
                        }
                    });

        } catch (Throwable t) {
            Platform.runLater(() -> {
                statusLabel.setText("VLC Init Error: Is VLC installed? " + t.getMessage());
                statusLabel.setStyle("-fx-text-fill: red;");
            });
            t.printStackTrace();
        }
    }

    @FXML
    private void onLoadClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video File");
        File file = fileChooser.showOpenDialog(statusLabel.getScene().getWindow());
        if (file != null) {
            if (mediaPlayer != null) {
                currentFile = file;
                statusLabel.setText("Loaded: " + file.getName());
                playButton.setDisable(false);
            } else {
                statusLabel.setText("Error: VLC not initialized.");
            }
        }
    }

    @FXML
    private void onPlayClicked() {
        if (mediaPlayer == null)
            return;

        if (mediaPlayer.status().isPlaying()) {
            mediaPlayer.controls().pause();
        } else {
            if (currentFile != null) {
                // Check if we need to start fresh or resume
                if (mediaPlayer.status().length() == -1 || mediaPlayer.status().state().toString().equals("STOPPED")) {
                    startPlayback();
                } else {
                    mediaPlayer.controls().play();
                }
            }
        }
    }

    private void startPlayback() {
        // 1. Local Playback (Visuals) + Audio Callback (PCM Capture)
        // We use "S16L" = Signed 16-bit Little Endian, 44100Hz, Stereo
        mediaPlayer.audio().callback("S16L", 44100, 2, new AudioCallback() {
            @Override
            public void play(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer, Pointer samples, int sampleCount,
                    long pts) {
                // Calculate buffer size: samples * channels (2) * bytesPerSample (2 for S16)
                int bufferSize = sampleCount * 4;
                byte[] data = samples.getByteArray(0, bufferSize);

                // Broadcast to LAN clients
                LocalServer server = serverService.getServer();
                if (server != null) {
                    server.broadcastAudio(data);
                }
            }

            @Override
            public void pause(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer, long pts) {
            }

            @Override
            public void resume(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer, long pts) {
            }

            @Override
            public void flush(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer, long pts) {
            }

            @Override
            public void drain(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer) {
            }

            @Override
            public void setVolume(float volume, boolean muted) {
                // no-op (volume control not needed for LAN streaming)
            }
        });

        mediaPlayer.media().play(currentFile.getAbsolutePath());
    }

    private void broadcastState() {
        LocalServer server = serverService.getServer();
        if (server != null) {
            server.broadcastState(
                    mediaPlayer.status().isPlaying(),
                    mediaPlayer.status().time(),
                    System.currentTimeMillis());
        }
    }

    public void stopServer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        if (mediaPlayerFactory != null) {
            mediaPlayerFactory.release();
        }
        serverService.stopServer();
    }
}
