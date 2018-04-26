/* JSON object for building subscription query */


package com.pingwinno.SubscriptionHandler;

import com.google.gson.annotations.SerializedName;

public class JsonSubObject {

    @SerializedName("hub.mode")
    private String hubMode;
    @SerializedName("hub.topic")
    private String hubTopic;
    @SerializedName("hub.callback")
    private String hubCallback;
    @SerializedName("hub.lease_seconds")
    private int hubLeaseSeconds;
   /* @SerializedName("hub.secret")
    private String hubSecret;
*/

public JsonSubObject (String hubMode, String hubTopic, String hubCallback, int hubLeaseSeconds )
{
    this.hubTopic = hubTopic;
    this.hubCallback = hubCallback;
    this.hubLeaseSeconds = hubLeaseSeconds;
    this.hubMode = hubMode;
    //this.hubSecret = hubSecret;


}

}
