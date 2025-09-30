package com.company.mtbp.inventory.config;

import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class H2ConsoleServerConfigTest {

    private H2ConsoleServerConfig config;

    @BeforeEach
    void setUp() {
        config = new H2ConsoleServerConfig();
        config.h2Port = "9092";
    }

    @Test
    void start_shouldStartH2Console() throws SQLException {
        Server mockServer = mock(Server.class);

        try (MockedStatic<Server> mockedStatic = mockStatic(Server.class)) {
            mockedStatic.when(() -> Server.createWebServer("-web", "-webAllowOthers", "-webPort", "9092"))
                    .thenReturn(mockServer);
            when(mockServer.start()).thenReturn(mockServer);

            config.start();

            // verify start() was called
            verify(mockServer).start();
        }
    }

    @Test
    void stop_shouldStopH2Console() {
        Server mockServer = mock(Server.class);
        config.webServer = mockServer;

        config.stop();

        verify(mockServer).stop();
    }

    @Test
    void stop_shouldNotFailIfServerIsNull() {
        config.webServer = null;

        assertDoesNotThrow(() -> config.stop());
    }

    @Test
    void start_andStop_withEvents() throws SQLException {
        Server mockServer = mock(Server.class);

        try (MockedStatic<Server> mockedStatic = mockStatic(Server.class)) {
            mockedStatic.when(() -> Server.createWebServer("-web", "-webAllowOthers", "-webPort", "9092"))
                    .thenReturn(mockServer);
            when(mockServer.start()).thenReturn(mockServer);

            config.start();
            assertNotNull(config.webServer);

            config.stop();
            verify(mockServer).stop();
        }
    }
}
