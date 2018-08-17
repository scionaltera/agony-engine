package com.agonyengine.model.actor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ConnectionTest {
    private Connection connection;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        connection = new Connection();
    }

    @Test
    public void testId() {
        UUID id = UUID.randomUUID();

        connection.setId(id);

        assertEquals(id, connection.getId());
    }

    @Test
    public void testAccount() {
        connection.setAccount("account");

        assertEquals("account", connection.getAccount());
    }

    @Test
    public void testSessionUsername() {
        connection.setSessionUsername("username");

        assertEquals("username", connection.getSessionUsername());
    }

    @Test
    public void testSessionId() {
        connection.setSessionId("id");

        assertEquals("id", connection.getSessionId());
    }

    @Test
    public void testRemoteIpAddress() {
        connection.setRemoteIpAddress("remote");

        assertEquals("remote", connection.getRemoteIpAddress());
    }

    @Test
    public void testDisconnectedDate() {
        Date date = new Date();

        connection.setDisconnectedDate(date);

        assertEquals(date, connection.getDisconnectedDate());
    }
}
