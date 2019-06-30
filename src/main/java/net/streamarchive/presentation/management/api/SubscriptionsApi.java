package net.streamarchive.presentation.management.api;


import net.streamarchive.application.SubscriptionRequest;
import net.streamarchive.infrastructure.SettingsProperties;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    SettingsProperties settingsProperties;

    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    public SubscriptionsApi(SubscriptionRequest subscriptionRequest, SettingsProperties settingsProperties) {
        this.subscriptionRequest = subscriptionRequest;
        this.settingsProperties = settingsProperties;
    }

    /**
     * Method returns list of current active subscriptions.
     *
     * @return list of current active subscriptions.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String[] getTimers() {
        return settingsProperties.getUsers();
    }

    /**
     * Method adds new chanel subscription.
     *
     * @param user name of chanel
     */
    @RequestMapping(value = "/{user}", method = RequestMethod.PUT)
    public void addSubscription(@PathVariable("user") String user) {
        try {
            subscriptionRequest.sendSubscriptionRequest(user);
        } catch (IOException e) {
            throw new InternalServerErrorException();
        }
        settingsProperties.addUser(user);
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
    private class InternalServerErrorException extends RuntimeException {
    }

}
