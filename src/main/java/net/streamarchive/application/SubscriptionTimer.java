package net.streamarchive.application;

import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.handlers.misc.HashHandler;
import net.streamarchive.infrastructure.models.Streamer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class SubscriptionTimer {
    private final
    SubscriptionRequest subscriptionRequest;
    private final
    HashHandler hashHandler;
    private final
    SettingsProvider settingsProperties;

    public SubscriptionTimer(SubscriptionRequest subscriptionRequest, HashHandler hashHandler, SettingsProvider settingsProperties) {
        this.subscriptionRequest = subscriptionRequest;
        this.hashHandler = hashHandler;
        this.settingsProperties = settingsProperties;
    }

    @Scheduled(fixedRate = 86400000)
    public void doSubscriptions() throws IOException {
        if (settingsProperties.isInitialized()) {
            log.info("Start subscribing to webhooks...");
            hashHandler.generateKey();
            for (Streamer streamer : settingsProperties.getStreamers()) {
                subscriptionRequest.sendSubscriptionRequest(streamer.getName());
            }
        } else {
            log.warn("Settings aren't loaded. Can't subscribe to webhooks");
        }
    }
}
