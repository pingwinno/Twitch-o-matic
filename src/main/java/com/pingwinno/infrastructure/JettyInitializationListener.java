package com.pingwinno.infrastructure;

import com.pingwinno.application.SubscriptionRequestTimer;
import com.pingwinno.application.twitch.playlist.handler.UserIdGetter;
import com.pingwinno.infrastructure.models.SubscriptionQueryModel;
import org.eclipse.jetty.util.component.LifeCycle;

import java.io.IOException;
import java.util.logging.Logger;

public class JettyInitializationListener implements LifeCycle.Listener {
    private static Logger log = Logger.getLogger(JettyInitializationListener.class.getName());

    @Override
    public void lifeCycleStarting(LifeCycle lifeCycle) {

    }

    @Override
    public void lifeCycleStarted(LifeCycle lifeCycle) {

        //subscribe request

        SubscriptionQueryModel json;
        try {
            json = new SubscriptionQueryModel("subscribe",
                    "https://api.twitch.tv/helix/streams?user_id=" +
                            UserIdGetter.getUserId(SettingsProperties.getUser()),
                    SettingsProperties.getCallbackAddress(), 864000);
            SubscriptionRequestTimer subscriptionQuery =
                    new SubscriptionRequestTimer("https://api.twitch.tv/helix/webhooks/hub", json);
            log.info("Sending subscription query");
            subscriptionQuery.sendSubscriptionRequest();
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
