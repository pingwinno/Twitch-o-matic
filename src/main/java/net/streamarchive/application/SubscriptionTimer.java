package net.streamarchive.application;

import lombok.SneakyThrows;

import java.util.TimerTask;

public class SubscriptionTimer extends TimerTask {

    private SubscriptionTask subscriptionTask;

    public SubscriptionTimer(SubscriptionTask subscriptionTask) {
        this.subscriptionTask = subscriptionTask;
    }


    @SneakyThrows
    @Override
    public void run() {
        subscriptionTask.doSubscriptions();
    }
}
