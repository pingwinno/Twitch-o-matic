package com.pingwinno.application;


import com.google.gson.Gson;
import com.pingwinno.infrastructure.HttpSeviceHelper;
import com.pingwinno.infrastructure.models.SubscriptionQueryModel;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;


public class SubscriptionRequestTimer extends TimerTask {

    private static Logger log = Logger.getLogger(SubscriptionRequestTimer.class.getName());
    private String serverAddress;
    private String postData;
    private int resubscribingPeriod;


    public SubscriptionRequestTimer(String serverAddress, SubscriptionQueryModel subscriptionModel) {
        Gson gson = new Gson();
        this.serverAddress = serverAddress;
        this.postData = gson.toJson(subscriptionModel);
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
            log.info("Subscription query send.");
            httpSeviceHelper.getService(httpPost, true);
            log.info("Waiting for hub.challenge request");
            httpSeviceHelper.close();
        } catch (IOException e) {
            log.severe("Subscription timer request failed. " + e);
        }
    }
}
