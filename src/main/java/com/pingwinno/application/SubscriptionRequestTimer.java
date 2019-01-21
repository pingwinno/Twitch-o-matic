package com.pingwinno.application;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.infrastructure.HttpSeviceHelper;
import com.pingwinno.infrastructure.models.SubscriptionQueryModel;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class SubscriptionRequestTimer extends TimerTask {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(StorageHelper.class.getName());
    private String serverAddress;
    private String postData;
    private int resubscribingPeriod;


    public SubscriptionRequestTimer(String serverAddress, SubscriptionQueryModel subscriptionModel) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.serverAddress = serverAddress;
        this.postData = mapper.writeValueAsString(subscriptionModel);
        this.resubscribingPeriod = subscriptionModel.getHubLeaseSeconds() * 1000;
    }

    public void sendSubscriptionRequest() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(this, 0, resubscribingPeriod);

    }

    public void run() {
        try {
            HttpSeviceHelper httpSeviceHelper = new HttpSeviceHelper();
            HttpPost httpPost = new HttpPost(serverAddress);
            StringEntity postBody = new StringEntity(postData);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Client-ID", "4zswqk0crwt2wy4b76aaltk2z02m67");
            httpPost.setEntity(postBody);
            log.debug("Subscription query send.");
            httpSeviceHelper.getService(httpPost, true);
            log.debug("Waiting for hub.challenge request");
            httpSeviceHelper.close();
        } catch (IOException e) {
            log.error("Subscription timer request failed. {}", e);
        }
    }
}
