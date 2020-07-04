package net.streamarchive.application.twitch.subscriptions;

import net.streamarchive.infrastructure.handlers.misc.AfterApplicationStartupRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Timer;

@Service
public class SubscriptionService implements AfterApplicationStartupRunnable {

    @Value("${net.streamarchive.subsctiption.delay}")
    private int delay;

    @Autowired
    private SubscriptionHandler subscriptionHandler;

    private void schedule() {
        Timer timer = new Timer();
        timer.schedule(new SubscriptionTimer(subscriptionHandler), 2000, delay * 1000);
    }

    @Override
    public void run() {
        schedule();
    }
}
