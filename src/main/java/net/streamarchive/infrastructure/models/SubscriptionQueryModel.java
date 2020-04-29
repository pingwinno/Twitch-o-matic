package net.streamarchive.infrastructure.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//subscription query object
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionQueryModel {

    @JsonProperty("hub.mode")
    private String hubMode;
    @JsonProperty("hub.topic")
    private String hubTopic;
    @JsonProperty("hub.callback")
    private String hubCallback;
    @JsonProperty("hub.lease_seconds")
    private int hubLeaseSeconds;
    @JsonProperty("hub.secret")
    private String hubSecret;
}
