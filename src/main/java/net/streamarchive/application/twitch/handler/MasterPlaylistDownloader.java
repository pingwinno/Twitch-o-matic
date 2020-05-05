package net.streamarchive.application.twitch.handler;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.exceptions.StreamNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class MasterPlaylistDownloader {

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SettingsProvider settingsProperties;

    public String getPlaylist(String vodId, String quality) throws IOException, StreamNotFoundException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Client-ID", settingsProperties.getClientID());
        httpHeaders.add("Accept", "application/vnd.twitchtv.v5+json");
        HttpEntity<String> requestEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.twitch.tv/kraken/videos/" + vodId,
                HttpMethod.GET, requestEntity, String.class);

        JsonNode jsonNode = objectMapper.readTree(Objects.requireNonNull(responseEntity.getBody()));

        String previewUrl = jsonNode.get("animated_preview_url").asText();
        if (previewUrl != null) {
            Map<String, String> availableQualities = objectMapper.convertValue(jsonNode.get("resolutions"), new TypeReference<>() {
            });
            quality = QualityValidator.validate(quality, availableQualities);
            String streamLink = previewUrl.substring(0, previewUrl.lastIndexOf("storyboards")) + quality + "/index-dvr.m3u8";
            log.trace("Stream link is: {}", streamLink);
            return streamLink;
        }
        if (responseEntity.getStatusCode().value() == 404) {
            throw new StreamNotFoundException("Stream " + vodId + " not found");
        }
        throw new IOException("Can't get substream link");
    }
}
