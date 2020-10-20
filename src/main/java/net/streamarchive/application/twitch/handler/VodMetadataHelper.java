package net.streamarchive.application.twitch.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.streamarchive.application.DateConverter;
import net.streamarchive.application.twitch.oauth.TwitchOAuthHandler;
import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.exceptions.StreamNotFoundException;
import net.streamarchive.infrastructure.models.QualityMetadata;
import net.streamarchive.infrastructure.models.StreamDataModel;
import net.streamarchive.infrastructure.models.video.TwitchVideoModel;
import net.streamarchive.infrastructure.models.video.VodType;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class VodMetadataHelper {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SettingsProvider settingsProperties;

    @Autowired
    private TwitchOAuthHandler twitchOAuthHandler;

    ObjectMapper objectMapper = new ObjectMapper();

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
        httpHeaders.add("Accept", "application/vnd.twitchtv.v5+json");
        HttpEntity<String> requestEntity = new HttpEntity<>("", httpHeaders);
        TwitchVideoModel videoModel;
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.twitch.tv/kraken/videos/" + vodId,
                    HttpMethod.GET, requestEntity, String.class);

            videoModel = objectMapper.readValue(responseEntity.getBody(), TwitchVideoModel.class);
        } catch (HttpClientErrorException.NotFound | JsonProcessingException e) {
            log.debug(e.toString());
            throw new StreamNotFoundException("Stream " + vodId + " not found", e);
        }

        StreamDataModel streamMetadata = new StreamDataModel();
        log.trace("{}", videoModel);

        try {
            streamMetadata.setVodId(vodId);
            streamMetadata.setTitle(videoModel.getTitle());
            streamMetadata.setDate(DateConverter.convert(videoModel.getRecordedAt()));
            var previewUrl = videoModel.getAnimatedPreviewUrl();
            streamMetadata.setStreamerName(videoModel.getChannel().getName());
            streamMetadata.setBaseUrl(previewUrl.substring(0, previewUrl.lastIndexOf("storyboards")));
            streamMetadata.setPreviewUrl(videoModel.getPreview().getLarge());
            streamMetadata.setGame(videoModel.getGame());
            streamMetadata.setVodType(VodType.valueOf(videoModel.getBroadcastType().toUpperCase()));
            streamMetadata.setDuration(videoModel.getLength());
            streamMetadata.setQualities(mapQualities(settingsProperties.getUser(
                    streamMetadata.getStreamerName()).getQualities(), videoModel));


        } catch (IllegalStateException | JSONException e) {
            log.error("{}", e);
            throw new StreamNotFoundException("Stream " + vodId + "not found");
        }

        return streamMetadata;
    }

    public StreamDataModel getVodMetadata(StreamDataModel streamMetadata) throws StreamNotFoundException {
        int vodId = streamMetadata.getVodId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Client-ID", settingsProperties.getClientID());
        httpHeaders.add("Accept", "application/vnd.twitchtv.v5+json");
        HttpEntity<String> requestEntity = new HttpEntity<>("", httpHeaders);
        TwitchVideoModel videoModel;
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.twitch.tv/kraken/videos/" + vodId,
                    HttpMethod.GET, requestEntity, String.class);

            videoModel = objectMapper.readValue(responseEntity.getBody(), TwitchVideoModel.class);
        } catch (HttpClientErrorException.NotFound | JsonProcessingException e) {
            log.debug(e.toString());
            throw new StreamNotFoundException("Stream " + vodId + " not found");
        }

        log.trace("{}", videoModel);

        try {
            streamMetadata.setVodId(vodId);
            streamMetadata.setTitle(videoModel.getTitle());
            streamMetadata.setDate(DateConverter.convert(videoModel.getRecordedAt()));
            var previewUrl = videoModel.getAnimatedPreviewUrl();
            streamMetadata.setBaseUrl(previewUrl.substring(0, previewUrl.lastIndexOf("storyboards")));
            streamMetadata.setGame(videoModel.getGame());
            streamMetadata.setDuration(videoModel.getLength());
            streamMetadata.setVodType(VodType.valueOf(videoModel.getBroadcastType().toUpperCase()));
            streamMetadata.setQualities(mapQualities(settingsProperties.getUser(
                    streamMetadata.getStreamerName()).getQualities(), videoModel));


        } catch (IllegalStateException | JSONException e) {
            log.error("{}", e);
            throw new StreamNotFoundException("Stream " + vodId + "not found");
        }

        return streamMetadata;
    }

    public boolean isRecording(int vodId) throws InterruptedException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Client-ID", settingsProperties.getClientID());
        httpHeaders.add("Accept", "application/vnd.twitchtv.v5+json");
        HttpEntity<String> requestEntity = new HttpEntity<>("", httpHeaders);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.twitch.tv/kraken/videos/" + vodId,
                    HttpMethod.GET, requestEntity, String.class);

            JSONObject jsonObj =
                    new JSONObject(responseEntity.getBody());
            log.trace("{}", jsonObj);

            return jsonObj.get("status").toString().equals("recording");
        } catch (HttpClientErrorException.NotFound e) {
            throw new StreamNotFoundException("Stream " + vodId + " not found");
        }
    }

    private Map<String, QualityMetadata> mapQualities(List<String> settingsQualities, TwitchVideoModel videoModel) {
        var resolutions = videoModel.getResolutions();
        var fpsList = videoModel.getFps();
        Map<String, QualityMetadata> availableQualities = new HashMap<>();
        for (String quality : settingsQualities) {
            String availableQuality = QualityValidator.validate(quality, resolutions.keySet());
            if (Objects.nonNull(availableQuality)) {
                availableQualities.put(availableQuality, new QualityMetadata(resolutions.get(availableQuality),
                        String.valueOf(fpsList.get(availableQuality))));
            }
        }
        if (CollectionUtils.isEmpty(availableQualities)) {
            availableQualities.put("chunked", new QualityMetadata("1920x1080", "30"));
        }
        return availableQualities;
    }

}
