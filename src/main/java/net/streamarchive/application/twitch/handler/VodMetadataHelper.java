package net.streamarchive.application.twitch.handler;

import net.streamarchive.application.DateConverter;
import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.exceptions.StreamNotFoundException;
import net.streamarchive.infrastructure.models.StreamDataModel;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class VodMetadataHelper {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(VodMetadataHelper.class.getName());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SettingsProvider settingsProperties;

    @Autowired
    private TwitchOAuthHandler twitchOAuthHandler;

    public StreamDataModel getLastVod(long userId) throws StreamNotFoundException {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Client-ID", settingsProperties.getClientID());
        httpHeaders.add("Authorization", "Bearer " + twitchOAuthHandler.getAccessToken());
        HttpEntity<String> requestEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.twitch.tv/helix/videos?user_id=" + userId,
                HttpMethod.GET, requestEntity, String.class);
        JSONObject jsonObj =
                new JSONObject(responseEntity.getBody());
        StreamDataModel streamMetadata = new StreamDataModel();
        if (jsonObj.getJSONArray("data") != null) {
            streamMetadata = getVodMetadata(Integer.parseInt(jsonObj.getJSONArray("data")
                    .getJSONObject(0).get("id").toString()));
        }
        return streamMetadata;
    }

    public StreamDataModel getVodMetadata(int vodId) throws StreamNotFoundException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Client-ID", settingsProperties.getClientID());
        httpHeaders.add("Authorization", "Bearer " + twitchOAuthHandler.getAccessToken());
        httpHeaders.add("Accept", "application/vnd.twitchtv.v5+json");
        HttpEntity<String> requestEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange("https://api.twitch.tv/kraken/videos/" + vodId,
                    HttpMethod.GET, requestEntity, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.debug(e.toString());
            throw new StreamNotFoundException("Stream " + vodId + " not found");
        }
        JSONObject dataObj =
                new JSONObject(responseEntity.getBody());


        StreamDataModel streamMetadata = new StreamDataModel();
        log.trace("{}", dataObj);
        if (!dataObj.toString().equals("")) {
            try {
                streamMetadata.setVodId(vodId);
                streamMetadata.setTitle(dataObj.get("title").toString());
                streamMetadata.setStreamerName(dataObj.getJSONObject("channel").get("name").toString());
                streamMetadata.setDate(DateConverter.convert(dataObj.get("recorded_at").toString()));

                streamMetadata.setBaseUrl((dataObj.getJSONObject("preview").get("large")).toString());
                if (!dataObj.get("game").toString().equals("")) {
                    streamMetadata.setGame(dataObj.get("game").toString());
                } else {
                    streamMetadata.setGame("");
                }
            } catch (IllegalStateException | JSONException e) {
                log.error("{}", e);
                throw new StreamNotFoundException("Stream " + vodId + "not found");
            }
        } else {
            throw new StreamNotFoundException("Stream " + vodId + "not found");
        }
        return streamMetadata;
    }

    public boolean isRecording(int vodId) throws InterruptedException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Client-ID", settingsProperties.getClientID());
        httpHeaders.add("Accept", "application/vnd.twitchtv.v5+json");
        HttpEntity<String> requestEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.twitch.tv/kraken/videos/" + vodId,
                HttpMethod.GET, requestEntity, String.class);

        JSONObject jsonObj =
                new JSONObject(responseEntity.getBody());
        log.trace("{}", jsonObj);

        return jsonObj.get("status").toString().equals("recording");
    }

}
