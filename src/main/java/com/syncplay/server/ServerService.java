package com.syncplay.server;

public class ServerService {
    private static ServerService instance;
    private final LocalServer server;
    private boolean isRunning = false;

    private ServerService() {
        this.server = new LocalServer(8080);
    }

    public static synchronized ServerService getInstance() {
        if (instance == null) {
            instance = new ServerService();
        }
        return instance;
    }

    public void startServer() throws ServerStartupException {
        if (!isRunning) {
            server.start();
            isRunning = true;
        }
    }

    public void stopServer() {
        if (isRunning) {
            server.stop();
            isRunning = false;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public LocalServer getServer() {
        return server;
    }
}
