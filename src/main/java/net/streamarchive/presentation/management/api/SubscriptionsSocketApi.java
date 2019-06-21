package net.streamarchive.presentation.management.api;

import net.streamarchive.application.SubscriptionRequestTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * API sends update of subscriptions list of client.
 */
@Service
public class SubscriptionsSocketApi {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public SubscriptionsSocketApi(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendUpdate(Map<String, Instant> message) {
        Map<String, Long> timers = new HashMap<>();
        for (Map.Entry<String, Instant> timer : message.entrySet()) {
            timers.put(timer.getKey(), SubscriptionRequestTimer.HUB_LEASE - Duration.between(timer.getValue(), Instant.now()).getSeconds());
        }
        this.messagingTemplate.convertAndSend("/topic/subscriptions", timers);
    }
}
