package net.streamarchive.presentation.management.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.streamarchive.application.SubscriptionRequestTimer;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API sends update of subscriptions list of client.
 */
@ServerEndpoint(value = "/subscriptions/")
public class SubscriptionsSocketApi {

    private static List<Session> sessions = new ArrayList<>();

    /**
     * Method sends list with updated subscriptions
     *
     * @param message for sending to client
     * @throws IOException
     */
    public static void updateState(Map<String, Instant> message) throws IOException {
        for (Session session : sessions) {
            Map<String, Long> timers = new HashMap<>();
            for (Map.Entry<String, Instant> timer : message.entrySet()) {
                timers.put(timer.getKey(), SubscriptionRequestTimer.HUB_LEASE - Duration.between(timer.getValue(), Instant.now()).getSeconds());
            }
            session.getBasicRemote().sendText(new ObjectMapper().writeValueAsString(timers));
        }
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        // Get session and StreamStatusSocketApi connection
        sessions.add(session);

    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // Handle new messages

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
