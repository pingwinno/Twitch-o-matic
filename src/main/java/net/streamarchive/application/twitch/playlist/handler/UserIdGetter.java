package net.streamarchive.application.twitch.playlist.handler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.logging.Logger;

public class UserIdGetter {

    private static Logger log = Logger.getLogger(UserIdGetter.class.getName());
    private static RestTemplate restTemplate = new RestTemplate();

    public static long getUserId(String user) throws IOException, InterruptedException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Client-ID", "eanof9ptu3k9448ukqe85cctiic8gm");
        HttpEntity<String> requestEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.twitch.tv/helix/users?login=" + user,
                HttpMethod.GET, requestEntity, String.class);
        log.fine(responseEntity.toString());
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
