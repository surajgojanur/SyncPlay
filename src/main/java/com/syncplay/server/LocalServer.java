package com.syncplay.server;

import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LocalServer {
    private static final Logger logger = LoggerFactory.getLogger(LocalServer.class);
    private Javalin app;
    private final int port;
    private final Set<WsContext> clients = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public LocalServer(int port) {
        this.port = port;
    }

    public void start() throws ServerStartupException {
        logger.info("LocalServer.start() called on port " + port);
        // Log stack trace to identify who called this
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 2) {
            logger.info(
                    "LocalServer started by: " + stackTrace[2].getClassName() + "." + stackTrace[2].getMethodName());
        }

        if (app != null) {
            return;
        }
        if (port == 8081) {
            throw new RuntimeException(
                    "CRITICAL ERROR: LocalServer attempted to bind to port 8081, which is reserved for VLC audio streaming!");
        }

        app = Javalin.create(config -> {
            config.staticFiles.add("/web"); // Serves from src/main/resources/web
            config.showJavalinBanner = false;
        });

        app.ws("/ws", ws -> {
            ws.onConnect(ctx -> {
                logger.info("Client connected: " + ctx.getSessionId());
                clients.add(ctx);
                // Send initial media info if available (TODO)
            });
            ws.onClose(ctx -> {
                logger.info("Client disconnected: " + ctx.getSessionId());
                clients.remove(ctx);
            });
            ws.onMessage(ctx -> {
                // Handle client messages (PING, etc.)
                String message = ctx.message();
                logger.debug("Received: " + message);
            });
            ws.onError(ctx -> {
                logger.error("Error in WS connection", ctx.error());
                clients.remove(ctx);
            });
        });

        app.ws("/audio", ws -> {
            ws.onConnect(ctx -> {
                logger.info("Audio Client connected: " + ctx.getSessionId());
                clients.add(ctx);
            });
            ws.onClose(ctx -> {
                clients.remove(ctx);
            });
            ws.onError(ctx -> {
                clients.remove(ctx);
            });
        });

        try {
            app.start(port);
            logger.info("Server started on port " + port);
        } catch (Exception e) {
            String msg = "Failed to bind to port " + port + ". Is it already in use?";
            logger.error(msg, e);
            throw new ServerStartupException(msg, e);
        }
    }

    public void stop() {
        if (app != null) {
            app.stop();
            app = null;
            clients.clear();
            logger.info("Server stopped");
        }
    }

    public void broadcast(String message) {
        for (WsContext client : clients) {
            if (client.session.isOpen()) {
                client.send(message);
            }
        }
    }

    public void broadcastAudio(byte[] audioData) {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(audioData);
        for (WsContext client : clients) {
            if (client.session.isOpen()) {
                client.send(buffer);
            }
        }
    }

    public void broadcastState(boolean isPlaying, long position, long timestamp) {
        String json = String.format(
                "{\"type\":\"STATE_UPDATE\", \"payload\":{\"isPlaying\":%b, \"timestamp\":%d, \"serverTime\":%d}}",
                isPlaying, position, timestamp);
        broadcast(json);
    }

    public int getClientCount() {
        return clients.size();
    }
}
