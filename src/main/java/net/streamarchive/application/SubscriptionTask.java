package net.streamarchive.application;

import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.handlers.misc.AfterApplicationStartupRunnable;
import net.streamarchive.infrastructure.handlers.misc.HashHandler;
import net.streamarchive.infrastructure.models.Streamer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Timer;

@Slf4j
@Service
public class SubscriptionTask implements AfterApplicationStartupRunnable {
    private final
    SubscriptionRequest subscriptionRequest;
    private final
    HashHandler hashHandler;
    private final
    SettingsProvider settingsProperties;
    private Timer timer = new Timer();

    @Value("${net.streamarchive.subsctiption.delay}")
    private int delay;

    public SubscriptionTask(SubscriptionRequest subscriptionRequest, HashHandler hashHandler, SettingsProvider settingsProperties) {
        this.subscriptionRequest = subscriptionRequest;
        this.hashHandler = hashHandler;
        this.settingsProperties = settingsProperties;
    }

    private void schedule() {
        timer.schedule(new SubscriptionTimer(this), 2000 , delay * 1000);
    }


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

    @Override
    public void run() {
        schedule();
    }
}
