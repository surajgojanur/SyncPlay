package com.syncplay.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

public class LocalServerTest {
    private LocalServer server;
    private static final int PORT = 8081; // Use different port for testing

    @BeforeEach
    public void setUp() throws ServerStartupException {
        server = new LocalServer(PORT);
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testServerStartsAndServesContent() throws Exception {
        URL url = new URL("http://localhost:" + PORT + "/");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        assertEquals(200, conn.getResponseCode(), "Server should return 200 OK for root");

        try (Scanner s = new Scanner(conn.getInputStream())) {
            String response = s.useDelimiter("\\A").next();
            assertTrue(response.contains("<title>SyncPlay Client</title>"),
                    "Response should contain index.html content");
        }
    }

    @Test
    public void testServerStateBroadcast() {
        // This is a basic test to ensure no exceptions are thrown when broadcasting
        // with no clients
        assertDoesNotThrow(() -> server.broadcastState(true, 1000, System.currentTimeMillis()));
    }
}
