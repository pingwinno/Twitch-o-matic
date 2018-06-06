package com.pingwinno.infrastructure;

import com.pingwinno.application.SubscriptionRequestTimer;
import com.pingwinno.application.UserIdGetter;
import org.eclipse.jetty.util.component.LifeCycle;

import java.io.IOException;

public class JettyInitializationListener implements LifeCycle.Listener {

    @Override
    public void lifeCycleStarting(LifeCycle lifeCycle) {

    }

    @Override
    public void lifeCycleStarted(LifeCycle lifeCycle) {

        //subscribe request
        UserIdGetter userIdGetter = new UserIdGetter();
        SubscriptionQueryModel json;
        try {
            json = new SubscriptionQueryModel("subscribe",
                    "https://api.twitch.tv/helix/streams?user_id=" + userIdGetter.getUserId(SettingsProperties.getUser()),
                    SettingsProperties.getCallbackAddress(), 864000);
            SubscriptionRequestTimer subscriptionQuery =
                    new SubscriptionRequestTimer("https://api.twitch.tv/helix/webhooks/hub", json);
            System.out.println("Sending subscription query");
            subscriptionQuery.sendSubscriptionRequest();
        } catch (IOException e) {
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
