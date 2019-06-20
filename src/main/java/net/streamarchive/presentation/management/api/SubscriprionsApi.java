package net.streamarchive.presentation.management.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.streamarchive.application.SubscriptionRequestTimer;
import net.streamarchive.application.twitch.playlist.handler.UserIdGetter;
import net.streamarchive.infrastructure.HashHandler;
import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.models.SubscriptionQueryModel;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
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
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public Response getTimers() {
        Map<String, Long> timers = new HashMap<>();
        for (Map.Entry<String, Instant> timer : SubscriptionRequestTimer.getTimers().entrySet()) {
            timers.put(timer.getKey(), SubscriptionRequestTimer.HUB_LEASE - Duration.between(timer.getValue(), Instant.now()).getSeconds());
        }
        try {
            return Response.status(Response.Status.OK)
                    .entity(new ObjectMapper().writeValueAsString(timers)).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Method adds new chanel subscription.
     * @param user name of chanel
     * @return adding operation response
     */
    @RequestMapping(value = "/{user}", method = RequestMethod.PUT)
    public Response addSubscription(@PathVariable("user") String user) {
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
            return Response.accepted().build();
        } catch (InterruptedException | IOException e) {
            return Response.notModified().build();
        }

    }

    /**
     * Method delete chanel subscription.
     * @param user name of chanel
     * @return deleting operation response
     */
    @DELETE
    @Path("/{user}")
    public Response removeSubscription(@PathParam("user") String user) {
        SubscriptionRequestTimer.stop(user);
        SettingsProperties.removeUser(user);
        return Response.accepted().build();
    }
}
