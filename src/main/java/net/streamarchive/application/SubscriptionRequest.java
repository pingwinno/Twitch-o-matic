package net.streamarchive.application;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.streamarchive.application.twitch.playlist.handler.UserIdGetter;
import net.streamarchive.infrastructure.HashHandler;
import net.streamarchive.infrastructure.HttpSevice;
import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.models.SubscriptionQueryModel;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SubscriptionRequest {

    private static final int HUB_LEASE = 10000 * 1000;
    private static org.slf4j.Logger log = LoggerFactory.getLogger(SubscriptionRequest.class.getName());

    private final HttpSevice httpSevice;
    private final
    HashHandler hashHandler;
    private final
    SettingsProperties settingsProperties;

    private CloseableHttpResponse httpResponse;

    public SubscriptionRequest(HttpSevice httpSevice, HashHandler hashHandler, SettingsProperties settingsProperties) {
        this.httpSevice = httpSevice;
        this.hashHandler = hashHandler;
        this.settingsProperties = settingsProperties;
    }

    public int sendSubscriptionRequest(String user) throws IOException {
        try {
            SubscriptionQueryModel subscriptionModel = new SubscriptionQueryModel("subscribe",
                    "https://api.twitch.tv/helix/streams?user_id=" +
                            UserIdGetter.getUserId(user),
                    settingsProperties.getCallbackAddress() + ":" + settingsProperties.getTwitchServerPort() +
                            "/handler/" + user, HUB_LEASE, hashHandler.getKey());
            log.trace("SubscriptionQueryModel: {}", subscriptionModel.toString());

            log.debug("Sending subscription query");


            HttpPost httpPost = new HttpPost("https://api.twitch.tv/helix/webhooks/hub");
            StringEntity postBody = new StringEntity(new ObjectMapper().writeValueAsString(subscriptionModel));
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Client-ID", "4zswqk0crwt2wy4b76aaltk2z02m67");
            httpPost.setEntity(postBody);
            log.debug("Subscription query send.");
            httpResponse = httpSevice.getService(httpPost, true);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            log.debug("Response code: {}", responseCode);

            log.debug("Waiting for hub.challenge request");

            return responseCode;

        } catch (IOException | InterruptedException e) {
            log.error("Subscription timer request failed. {}", e.toString());
            throw new IOException("Subscription failed");
        } finally {
            httpResponse.close();
            httpSevice.close();
        }

    }
}
