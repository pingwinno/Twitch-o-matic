package net.streamarchive.application;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.streamarchive.application.twitch.handler.UserIdGetter;
import net.streamarchive.infrastructure.SettingsProperties;
import net.streamarchive.infrastructure.handlers.misc.HashHandler;
import net.streamarchive.infrastructure.models.SubscriptionQueryModel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class SubscriptionRequest {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(SubscriptionRequest.class.getName());
    private final
    RestTemplate restTemplate;
    private final
    HashHandler hashHandler;
    private final
    SettingsProperties settingsProperties;
    private int hubLease = 86400;
    @Autowired
    private UserIdGetter userIdGetter;

    public SubscriptionRequest(HashHandler hashHandler, SettingsProperties settingsProperties, RestTemplate restTemplate) {
        this.hashHandler = hashHandler;
        this.settingsProperties = settingsProperties;
        this.restTemplate = restTemplate;
    }

    public int sendSubscriptionRequest(String user) throws IOException {

        try {
            SubscriptionQueryModel subscriptionModel = new SubscriptionQueryModel("subscribe",
                    "https://api.twitch.tv/helix/streams?user_id=" +
                            userIdGetter.getUserId(user),
                    settingsProperties.getCallbackAddress() +
                            "/handler/" + user, hubLease, hashHandler.getKey());
            log.trace("SubscriptionQueryModel: {}", subscriptionModel.toString());

            log.debug("Sending subscription query");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Client-ID",    settingsProperties.getClientID());
            httpHeaders.add("Content-Type", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(new ObjectMapper().writeValueAsString(subscriptionModel),
                    httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.twitch.tv/helix/webhooks/hub",
                    HttpMethod.POST, requestEntity, String.class);

            log.debug("Response code: {}", responseEntity.getStatusCode().value());

            log.debug("Waiting for hub.challenge request");

            return responseEntity.getStatusCode().value();

        } catch (IOException e) {
            log.error("Subscription timer request failed. {}", e.toString());
            throw new IOException("Subscription failed");
        }

    }
}
