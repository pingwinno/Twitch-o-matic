package net.streamarchive.application.twitch.subscriptions;

import lombok.SneakyThrows;

import java.util.TimerTask;

public class SubscriptionTimer extends TimerTask {

    private SubscriptionHandler subscriptionHandler;

    public SubscriptionTimer(SubscriptionHandler subscriptionHandler) {
        this.subscriptionHandler = subscriptionHandler;
    }


    @SneakyThrows
    @Override
    public void run() {
        subscriptionHandler.doSubscriptions();
    }
}
