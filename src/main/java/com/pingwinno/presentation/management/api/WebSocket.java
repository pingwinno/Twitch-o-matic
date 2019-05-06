package com.pingwinno.presentation.management.api;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint(value = "/websocket/")
public class WebSocket {

    private static List<Session> sessions = new ArrayList<>();

    public static void updateState(String message) throws IOException {
        for (Session session : sessions) {
            session.getBasicRemote().sendText(message);
        }
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        // Get session and WebSocket connection
        sessions.add(session);
        session.getBasicRemote().sendText("hello there");
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // Handle new messages
        System.out.print(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // WebSocket connection closes
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        sessions.remove(session);
    }
}
