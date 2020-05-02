package net.streamarchive.application.twitch.handler;


import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.SettingsProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class UserIdGetter {

    @Autowired
    private SettingsProvider settingsProperties;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TwitchOAuthHandler twitchOAuthHandler;

    public long getUserId(String user) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Client-ID", settingsProperties.getClientID());
        httpHeaders.add("Authorization", "Bearer " + twitchOAuthHandler.getAccessToken());
        HttpEntity<String> requestEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.twitch.tv/helix/users?login=" + user,
                HttpMethod.GET, requestEntity, String.class);
        log.trace(responseEntity.toString());
        JSONObject jsonObj =
                new JSONObject(responseEntity.getBody());
        long userId;
        try {
            JSONArray params = jsonObj.getJSONArray("data");
            JSONObject dataObj = params.getJSONObject(0);
            userId = Long.parseLong(dataObj.get("id").toString());

        } catch (JSONException exception) {
            userId = getUserId(user);
        }
        return userId;
    }
}
