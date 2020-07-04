package net.streamarchive.presentation.management.api;


import net.streamarchive.application.StorageHelper;
import net.streamarchive.application.twitch.subscriptions.SubscriptionRequest;
import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.models.Streamer;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API for chanel subscriptions management.
 * Endpoint {@code /subscriptions}
 */

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionsApi {

    private final
    SubscriptionRequest subscriptionRequest;
    private final
    SettingsProvider settingsProperties;
    private final
    StorageHelper storageHelper;

    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    public SubscriptionsApi(SubscriptionRequest subscriptionRequest, SettingsProvider settingsProperties, StorageHelper storageHelper) {
        this.subscriptionRequest = subscriptionRequest;
        this.settingsProperties = settingsProperties;
        this.storageHelper = storageHelper;
    }

    /**
     * Method returns list of current active subscriptions.
     *
     * @return list of current active subscriptions.
     */
    @RequestMapping(method = RequestMethod.GET)
    public Map<String, List<String>> getTimers() {

        Map<String, List<String>> users = new HashMap<>();
        for (Streamer streamer : settingsProperties.getStreamers()) {
            users.put(streamer.getName(), List.of(streamer.getQuality()));
        }
        return users;
    }

    /**
     * Method adds new chanel subscription.
     *
     * @param user    name of chanel
     * @param quality list of streams quality
     */
    @RequestMapping(value = "/{user}", method = RequestMethod.PUT)
    public void addSubscription(@PathVariable("user") String user, @RequestBody List<String> quality) {
        try {
            subscriptionRequest.sendSubscriptionRequest(user);
            Streamer streamerEntity = new Streamer();
            streamerEntity.setName(user.toLowerCase());
            quality.sort(Comparator.comparing(String::length));
            streamerEntity.setQuality(quality.get(0));
            settingsProperties.addStreamer(streamerEntity);
            storageHelper.creatingRecordedPath(user);
        } catch (IOException e) {
            throw new InternalServerErrorException();
        }
    }

    /**
     * Method delete chanel subscription.
     *
     * @param user name of chanel
     */

    @RequestMapping(value = "/{user}", method = RequestMethod.DELETE)
    public void removeSubscription(@PathVariable("user") String user) {
        settingsProperties.removeUser(user);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private static class InternalServerErrorException extends RuntimeException {
    }

}
