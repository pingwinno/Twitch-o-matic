package net.streamarchive.presentation.management.api;


import net.streamarchive.infrastructure.SettingsProperties;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API for chanel subscriptions management.
 * Endpoint {@code /subscriptions}
 */

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriprionsApi {
    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    /**
     * Method returns list of current active subscriptions.
     *
     * @return list of current active subscriptions.
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<JobExecutionContext> getTimers() throws SchedulerException {
        log.debug("tasklist is singleton {}", schedulerFactoryBean.isSingleton());
        schedulerFactoryBean.stop();
        log.debug("tasklist is singleton {}", schedulerFactoryBean.toString());
        return schedulerFactoryBean.getScheduler().getCurrentlyExecutingJobs();
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
