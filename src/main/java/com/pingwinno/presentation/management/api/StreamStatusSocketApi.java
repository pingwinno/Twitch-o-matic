package com.pingwinno.presentation.management.api;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint(value = "/status/")
public class StreamStatusSocketApi {

    private static List<Session> sessions = new ArrayList<>();

    public static void updateState(String message) throws IOException {
        for (Session session : sessions) {
            session.getBasicRemote().sendText(message);
        }
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        // Get session and StreamStatusSocketApi connection
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // StreamStatusSocketApi connection closes
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        sessions.remove(session);
    }
}
