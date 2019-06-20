package net.streamarchive;

import net.streamarchive.application.RecoveryRecordHandler;
import net.streamarchive.application.StorageHelper;
import net.streamarchive.application.SubscriptionRequestTimer;
import net.streamarchive.application.twitch.playlist.handler.UserIdGetter;
import net.streamarchive.infrastructure.HashHandler;
import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.models.SubscriptionQueryModel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
@EnableTransactionManagement

public class TwitchOMaticApplication {
    @Autowired
    RecoveryRecordHandler recoveryRecordHandler;

    public static void main(String[] args) {
        SpringApplication.run(TwitchOMaticApplication.class, args);
    }

    @PostConstruct
    public void init() throws IOException, InterruptedException {
        TwitchOMaticApplication.class.getResourceAsStream("log4j2.json");

        org.slf4j.Logger log = LoggerFactory.getLogger(TwitchOMaticApplication.class.getName());
        //use if direct connection to h2 needed
        // org.h2.tools.Server.createWebServer(new String[]{"-web","-webAllowOthers","-webPort","7071"}).start();

        log.info("Checking storage...");
        try {
            if (!StorageHelper.initialStorageCheck()) {
                System.exit(1);
            }
        } catch (IOException e) {
            log.error("Checking storage failed ", e);
        }

        SubscriptionQueryModel json;
        {
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


        }
        new Thread(() -> recoveryRecordHandler.recoverUncompletedRecordTask()).start();
    }
}
