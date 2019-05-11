package com.pingwinno.infrastructure;

import com.pingwinno.application.SubscriptionRequestTimer;
import com.pingwinno.application.twitch.playlist.handler.UserIdGetter;
import com.pingwinno.infrastructure.models.SubscriptionQueryModel;
import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JettyInitializationListener implements LifeCycle.Listener {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    @Override
    public void lifeCycleStarting(LifeCycle lifeCycle) {

    }

    @Override
    public void lifeCycleStarted(LifeCycle lifeCycle) {

        //subscribe request

        SubscriptionQueryModel json;
        try {
            HashHandler.generateKey();
            for (String user : SettingsProperties.getUsers()) {
                user = user.trim();
                json = new SubscriptionQueryModel("subscribe",
                        "https://api.twitch.tv/helix/streams?user_id=" +
                                UserIdGetter.getUserId(user),
                        SettingsProperties.getCallbackAddress() + ":" + SettingsProperties.getTwitchServerPort() +
                                "/handler/" + user, SubscriptionRequestTimer.HUB_LEASE, HashHandler.getKey());
                log.trace("SubscriptionQueryModel: {}", json.toString());
                SubscriptionRequestTimer subscriptionQuery =
                        new SubscriptionRequestTimer("https://api.twitch.tv/helix/webhooks/hub", json);
                log.debug("Sending subscription query");
                subscriptionQuery.sendSubscriptionRequest(user);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void lifeCycleFailure(LifeCycle lifeCycle, Throwable throwable) {

    }

    @Override
    public void lifeCycleStopping(LifeCycle lifeCycle) {

    }

    @Override
    public void lifeCycleStopped(LifeCycle lifeCycle) {

    }


}
