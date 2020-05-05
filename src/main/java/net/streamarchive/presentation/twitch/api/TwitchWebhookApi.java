package net.streamarchive.presentation.twitch.api;


import lombok.extern.slf4j.Slf4j;
import net.streamarchive.domain.service.WebhookRecordService;
import net.streamarchive.infrastructure.exceptions.StreamNotFoundException;
import net.streamarchive.infrastructure.handlers.misc.HashHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/handler")
public class TwitchWebhookApi {

    @Autowired
    HashHandler hashHandler;

    @Autowired
    private WebhookRecordService recordCreationService;


    @RequestMapping(value = "/{user}", method = RequestMethod.GET)
    public String getSubscriptionQuery(@RequestParam Map<String, String> allParams, @PathVariable("user") String user) {

        log.debug("Incoming challenge request");
        if (allParams != null) {
            log.debug("hub.mode {} ", allParams.get("hub.mode"));
            String hubMode = allParams.get("hub.mode");
            //handle denied response
            if (hubMode.equals("denied")) {
                String hubReason = allParams.get("hub.reason");

                log.warn("Subscription failed. Reason:{}", hubReason);
                return null;
            }
            //handle verify response
            else {
                log.debug("Subscription complete {} hub.challenge is:", allParams.get("hub.challenge"));
                log.info(" Twith-o-matic started for {}. Waiting for stream up", user);
                return allParams.get("hub.challenge");

            }
        } else {
            log.warn("Subscription query is not correct. Try restart Twitch-o-matic.");
        }

        return null;
    }

    @RequestMapping(value = "/{user}", method = RequestMethod.POST)
    public void handleStreamNotification(@RequestBody String notification
            , @RequestHeader("content-length") long length, @RequestHeader("X-Hub-Signature") String signature, @PathVariable("user") String user) throws InterruptedException, StreamNotFoundException, IOException {

        log.debug("Incoming stream up/down notification");

        log.debug("data length {} body length {} ", notification.length(), length);
        log.debug(notification);
        if (hashHandler.compare(signature, notification)) {
            log.debug("Hash confirmed");
            //check for active subscription
            recordCreationService.handleLiveNotification(user, notification);

        } else {
            log.error("Notification not accepted. Wrong hash.");
        }
        log.debug("Response ok");
    }


}


