package com.pingwinno.infrastructure;

import com.pingwinno.application.RecoveryRecordHandler;
import com.pingwinno.application.SubscriptionRequestTimer;
import com.pingwinno.application.twitch.playlist.handler.UserIdGetter;
import com.pingwinno.infrastructure.models.SubscriptionQueryModel;
import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public class JettyInitializationListener implements LifeCycle.Listener {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    @Override
    public void lifeCycleStarting(LifeCycle lifeCycle) {

    }

    @Override
    public void lifeCycleStarted(LifeCycle lifeCycle) {

        //subscribe request

        try {
            RecoveryRecordHandler.recoverUncompletedRecordTask();
        } catch (SQLException | IOException | InterruptedException e) {
            log.error("Can't recover list {}", e);
        }
        SubscriptionQueryModel json;
        try {
            HashHandler.generateKey();
            json = new SubscriptionQueryModel("subscribe",
                    "https://api.twitch.tv/helix/streams?user_id=" +
                            UserIdGetter.getUserId(SettingsProperties.getUser()),
                    SettingsProperties.getCallbackAddress() + ":"+ SettingsProperties.getTwitchServerPort() +
                            "/handler", 86400, HashHandler.getKey());
            log.trace("SubscriptionQueryModel: {}", json.toString());
            SubscriptionRequestTimer subscriptionQuery =
                    new SubscriptionRequestTimer("https://api.twitch.tv/helix/webhooks/hub", json);
            log.debug("Sending subscription query");
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
