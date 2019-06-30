package net.streamarchive.application;

import net.streamarchive.infrastructure.SettingsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SubscriptionTimer {
    @Autowired
    SubscriptionRequest subscriptionRequest;

    @Scheduled(fixedRate = 10000)
    public void doSubscription() throws IOException {
        
        for (String user : SettingsProperties.getUsers()) {
            subscriptionRequest.sendSubscriptionRequest(user);
        }
    }
}
