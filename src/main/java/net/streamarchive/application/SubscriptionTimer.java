package net.streamarchive.application;

import net.streamarchive.infrastructure.HashHandler;
import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.models.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SubscriptionTimer {
    private final
    SubscriptionRequest subscriptionRequest;
    private final
    HashHandler hashHandler;
    private final
    SettingsProperties settingsProperties;

    public SubscriptionTimer(SubscriptionRequest subscriptionRequest, HashHandler hashHandler, SettingsProperties settingsProperties) {
        this.subscriptionRequest = subscriptionRequest;
        this.hashHandler = hashHandler;
        this.settingsProperties = settingsProperties;
    }

    @Scheduled(fixedRate = 10000 * 1000)
    public void doSubscriptions() throws IOException {
        hashHandler.generateKey();
        for (User user : settingsProperties.getUsers()) {
            subscriptionRequest.sendSubscriptionRequest(user.getUser());
        }
    }
}
