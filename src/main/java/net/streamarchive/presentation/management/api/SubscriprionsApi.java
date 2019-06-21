package net.streamarchive.presentation.management.api;

import net.streamarchive.application.SubscriptionRequestTimer;
import net.streamarchive.application.twitch.playlist.handler.UserIdGetter;
import net.streamarchive.infrastructure.HashHandler;
import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.models.SubscriptionQueryModel;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * API for chanel subscriptions management.
 * Endpoint {@code /subscriptions}
 */

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriprionsApi {
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    /**
     * Method returns list of current active subscriptions.
     * @return list of current active subscriptions.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Map<String, Long> getTimers() {
        Map<String, Long> timers = new HashMap<>();
        for (Map.Entry<String, Instant> timer : SubscriptionRequestTimer.getTimers().entrySet()) {
            timers.put(timer.getKey(), SubscriptionRequestTimer.HUB_LEASE - Duration.between(timer.getValue(), Instant.now()).getSeconds());
        }
        return timers;
    }

    /**
     * Method adds new chanel subscription.
     * @param user name of chanel
     * @return adding operation response
     */
    @RequestMapping(value = "/{user}", method = RequestMethod.PUT)
    public void addSubscription(@PathVariable("user") String user) {
        try {
            SubscriptionQueryModel json = new SubscriptionQueryModel("subscribe",
                    "https://api.twitch.tv/helix/streams?user_id=" +
                            UserIdGetter.getUserId(user),
                    SettingsProperties.getCallbackAddress() + ":" + SettingsProperties.getTwitchServerPort() +
                            "/handler/" + user, SubscriptionRequestTimer.HUB_LEASE, HashHandler.getKey());
            log.trace("SubscriptionQueryModel: {}", json.toString());
            SubscriptionRequestTimer subscriptionQuery = new SubscriptionRequestTimer("https://api.twitch.tv/helix/webhooks/hub", json);
            subscriptionQuery.sendSubscriptionRequest(user);
            log.debug("Sending subscription query");
            SettingsProperties.addUser(user);

        } catch (InterruptedException | IOException e) {
            log.error("Can't start subscription for {}", user);
            throw new InternalServerErrorExeption();
        }
    }

    /**
     * Method delete chanel subscription.
     * @param user name of chanel
     * @return deleting operation response
     */

    @RequestMapping(value = "/{user}", method = RequestMethod.DELETE)
    public void removeSubscription(@PathVariable("user") String user) {
        SubscriptionRequestTimer.stop(user);
        SettingsProperties.removeUser(user);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private class InternalServerErrorExeption extends RuntimeException {
    }

}
