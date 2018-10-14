package com.pingwinno.infrastructure.models;


import com.fasterxml.jackson.annotation.JsonProperty;

//subscription query object
public class SubscriptionQueryModel {

    @JsonProperty("hub.mode")
    private String hubMode;
    @JsonProperty("hub.topic")
    private String hubTopic;
    @JsonProperty("hub.callback")
    private String hubCallback;
    @JsonProperty("hub.lease_seconds")
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
