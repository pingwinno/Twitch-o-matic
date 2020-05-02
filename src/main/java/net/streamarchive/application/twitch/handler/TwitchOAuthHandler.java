package net.streamarchive.application.twitch.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.exceptions.TwitchTokenProcessingException;
import net.streamarchive.infrastructure.models.TwitchAuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class TwitchOAuthHandler {
    private static final String OAUTH_ENDPOINT = "https://id.twitch.tv/oauth2/token";

    private TwitchAuthToken twitchAuthToken;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SettingsProvider settingsProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void requestAccessToken(String authorizationToken) {
        log.info("Requesting access token...");
        UriComponentsBuilder componentsBuilder = UriComponentsBuilder.fromHttpUrl(OAUTH_ENDPOINT)
                .queryParam("client_id", settingsProvider.getClientID())
                .queryParam("client_secret", settingsProvider.getClientSecret())
                .queryParam("code", authorizationToken)
                .queryParam("grant_type", "authorization_code")
                .queryParam("redirect_uri", settingsProvider.getOauthRedirectAddress() + "/twitch/oauth");

        try {
            twitchAuthToken = objectMapper.readValue(performRequest(componentsBuilder), TwitchAuthToken.class);
            log.info("Request complete");
        } catch (JsonProcessingException e) {
            throw new TwitchTokenProcessingException("Can't parse twitch response.", e);
        }
    }

    public void refreshAccessToken() {
        log.info("Refresh access token...");
        UriComponentsBuilder componentsBuilder = UriComponentsBuilder.fromHttpUrl(OAUTH_ENDPOINT)
                .queryParam("client_id", settingsProvider.getClientID())
                .queryParam("client_secret", settingsProvider.getClientSecret())
                .queryParam("refresh_token", twitchAuthToken.getRefreshToken())
                .queryParam("grant_type", "refresh_token");

        try {
            twitchAuthToken = objectMapper.readValue(performRequest(componentsBuilder), TwitchAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new TwitchTokenProcessingException("Can't parse twitch response.", e);
        }
    }

    public String getAccessToken() {
        log.debug("Token is requested by consumer");
        validateToken();
        return twitchAuthToken.getAccessToken();
    }

    private String performRequest(UriComponentsBuilder componentsBuilder) {
        log.debug("Perform token request...");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        ResponseEntity<String> response = restTemplate.exchange(
                componentsBuilder.encode().toUriString(),
                HttpMethod.POST,
                entity,
                String.class);
        if (response.getBody() == null) {
            log.error("Can't obtain access token");
            throw new TwitchTokenProcessingException("Can't obtain oAuth token");
        }
        return response.getBody();
    }

    private ResponseEntity<String> requestValidation() {
        log.debug("Validating access token..");
        if (twitchAuthToken == null) {
            throw new TwitchTokenProcessingException("Token is null. Please, load settings and get access token first");
        }
        HttpHeaders headers = new HttpHeaders();
        String url = "https://id.twitch.tv/oauth2/validate";
        headers.add("Authorization", "OAuth " + twitchAuthToken.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        log.debug("Access token validation complete");
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class);

    }

    public void validateToken() {
        if (requestValidation().getStatusCodeValue() == 401) {
            log.warn("Access token validation failed");
            log.info("Try to refresh access token...");
            refreshAccessToken();
        }
        if (requestValidation().getStatusCodeValue() != 200) {
            throw new TwitchTokenProcessingException(requestValidation().getStatusCode().toString());
        }
    }
}
