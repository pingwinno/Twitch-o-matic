package net.streamarchive.presentation.management.api;


import net.streamarchive.infrastructure.SettingsProperties;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
     *
     * @return list of current active subscriptions.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String[] getTimers() {

        return SettingsProperties.getUsers();
    }

    /**
     * Method adds new chanel subscription.
     *
     * @param user name of chanel
     * @return adding operation response
     */
    @RequestMapping(value = "/{user}", method = RequestMethod.PUT)
    public void addSubscription(@PathVariable("user") String user) {


    }

    /**
     * Method delete chanel subscription.
     *
     * @param user name of chanel
     * @return deleting operation response
     */

    @RequestMapping(value = "/{user}", method = RequestMethod.DELETE)
    public void removeSubscription(@PathVariable("user") String user) {

        SettingsProperties.removeUser(user);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private class InternalServerErrorExeption extends RuntimeException {
    }

}
