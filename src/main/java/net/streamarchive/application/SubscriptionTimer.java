package net.streamarchive.application;

import net.streamarchive.infrastructure.HashHandler;
import net.streamarchive.infrastructure.SettingsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SubscriptionTimer {
    @Autowired
    SubscriptionRequest subscriptionRequest;
    @Autowired
    HashHandler hashHandler;

    @Scheduled(fixedRate = 10000)
    public void doSubscriptions() throws IOException {
        hashHandler.generateKey();
        for (String user : SettingsProperties.getUsers()) {
            subscriptionRequest.sendSubscriptionRequest(user);
        }
    }
}
