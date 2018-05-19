package com.pingwinno.application;


import com.google.gson.Gson;
import com.pingwinno.infrastructure.SubscriptionQueryModel;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class SubscriptionRequestTimer extends TimerTask {

    private String serverAddress;
    private String postData;
    private int resubscribingPeriod;

    public SubscriptionRequestTimer(String serverAddress, SubscriptionQueryModel subscriptionModel) {
        Gson gson = new Gson();
        this.serverAddress = serverAddress;
        postData = gson.toJson(subscriptionModel);
        resubscribingPeriod = subscriptionModel.getHubLeaseSeconds() * 1000;
    }

    public void sendSubscriptionRequest() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(this, 10000, resubscribingPeriod);
    }

    public void run() {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(serverAddress);
            StringEntity postBody = new StringEntity(postData);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Client-ID", "4zswqk0crwt2wy4b76aaltk2z02m67");
            httpPost.setEntity(postBody);
            CloseableHttpResponse response = client.execute(httpPost);
            System.out.println("Response code " + response.getStatusLine().getStatusCode());
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
