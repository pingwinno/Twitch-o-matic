package com.pingwinno.application;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.infrastructure.HttpSevice;
import com.pingwinno.infrastructure.models.SubscriptionQueryModel;
import com.pingwinno.presentation.management.api.ServerStatusSocket;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


public class SubscriptionRequestTimer extends TimerTask {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(StorageHelper.class.getName());
    public static final int HUB_LEASE = 86400;
    private static Map<String, Timer> timers = new ConcurrentHashMap<>();
    private String serverAddress;
    private String postData;
    private int resubscribingPeriod;
    private static Map<String, Instant> timersStartTime = new ConcurrentHashMap<>();
    private String user;

    public SubscriptionRequestTimer(String serverAddress, SubscriptionQueryModel subscriptionModel) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.serverAddress = serverAddress;
        this.postData = mapper.writeValueAsString(subscriptionModel);
        this.resubscribingPeriod = subscriptionModel.getHubLeaseSeconds() * 1000;
    }

    public static void stop(String user) {
        timers.remove(user).cancel();
        timersStartTime.remove(user);
    }

    public static Map<String, Instant> getTimers() {
        return new HashMap<>(timersStartTime);
    }

    public void sendSubscriptionRequest(String user) {
        this.user = user;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(this, 0, resubscribingPeriod);
        timers.put(user, timer);
        timersStartTime.put(user, Instant.now());
    }

    public void run() {
        try {
            HttpSevice httpSevice = new HttpSevice();
            HttpPost httpPost = new HttpPost(serverAddress);
            StringEntity postBody = new StringEntity(postData);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Client-ID", "4zswqk0crwt2wy4b76aaltk2z02m67");
            httpPost.setEntity(postBody);
            log.debug("Subscription query send.");
            httpSevice.getService(httpPost, true);
            log.debug("Waiting for hub.challenge request");
            httpSevice.close();
            timersStartTime.replace(user, Instant.now());
            ServerStatusSocket.updateState(getTimers());

        } catch (IOException | InterruptedException e) {
            log.error("Subscription timer request failed. {}", e);
        }

    }
}
