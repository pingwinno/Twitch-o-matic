package net.streamarchive.presentation.management.api;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * API sends updates of status list to client.
 */
@ServerEndpoint(value = "/status/")
public class StreamStatusSocketApi {

    private static List<Session> sessions = new ArrayList<>();

    /**
     * @param message A JSON string with {@see StreamStatusModel}
     * @throws IOException
     */
    public static void updateState(String message) throws IOException {
        for (Session session : sessions) {
            session.getBasicRemote().sendText(message);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        // Get session and StreamStatusSocketApi connection
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {

    }

    @OnClose
    public void onClose(Session session) {
        // StreamStatusSocketApi connection closes
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session) {
        // Do error handling here
        sessions.remove(session);
    }
}
