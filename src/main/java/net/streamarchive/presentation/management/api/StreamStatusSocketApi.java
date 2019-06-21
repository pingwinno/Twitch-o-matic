package net.streamarchive.presentation.management.api;

import net.streamarchive.infrastructure.models.StatusDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * API sends updates of status list to client.
 */

@Service
public class StreamStatusSocketApi {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public StreamStatusSocketApi(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendUpdate(StatusDataModel statusDataModel) {
        this.messagingTemplate.convertAndSend("/topic/status", statusDataModel);
    }
}
