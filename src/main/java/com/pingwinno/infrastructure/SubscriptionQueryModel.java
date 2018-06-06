package com.pingwinno.infrastructure;

import com.google.gson.annotations.SerializedName;

//subscription query object
public class SubscriptionQueryModel {

    @SerializedName("hub.mode")
    private String hubMode;
    @SerializedName("hub.topic")
    private String hubTopic;
    @SerializedName("hub.callback")
    private String hubCallback;
    @SerializedName("hub.lease_seconds")
    private int hubLeaseSeconds;
    /* May used to sign notification payloads
    @SerializedName("hub.secret")
     private String hubSecret;*/

    public SubscriptionQueryModel(String hubMode, String hubTopic, String hubCallback, int hubLeaseSeconds) {
        this.hubTopic = hubTopic;
        this.hubCallback = hubCallback;
        this.hubLeaseSeconds = hubLeaseSeconds;
        this.hubMode = hubMode;
        //this.hubSecret = hubSecret;
    }

    public int getHubLeaseSeconds() {
        return hubLeaseSeconds;
    }
}
