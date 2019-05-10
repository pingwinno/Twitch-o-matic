package com.pingwinno.presentation.management.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.application.JdbcHandler;
import com.pingwinno.infrastructure.RecordThreadSupervisor;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ServerEndpoint(value = "/status/")
public class StatusUpdater {

    private static List<Session> sessions = new ArrayList<>();

    public static void updateState(String message) throws IOException {
        for (Session session : sessions) {
            session.getBasicRemote().sendText(message);
        }
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        // Get session and StatusUpdater connection
        sessions.add(session);
        session.getBasicRemote().sendText(new ObjectMapper().writeValueAsString(new JdbcHandler().selectAll()));
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // Handle new messages
        if (message.contains("stop")) {
            RecordThreadSupervisor.stop(UUID.fromString(message.split(":")[1].trim()));
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // StatusUpdater connection closes
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        sessions.remove(session);
    }
}
