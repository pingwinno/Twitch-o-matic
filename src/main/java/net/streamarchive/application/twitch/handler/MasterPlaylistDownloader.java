package net.streamarchive.application.twitch.handler;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Objects;

@Component
public class MasterPlaylistDownloader {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SettingsProvider settingsProperties;

    ObjectMapper objectMapper = new ObjectMapper();

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
            return previewUrl.substring(0, previewUrl.lastIndexOf("storyboards")) + quality + "/index-dvr.m3u8";
        }
        if (responseEntity.getStatusCode().value() == 404) {
            throw new StreamNotFoundException("Stream " + vodId + " not found");
        }
        throw new IOException("Can't get substream link");
    }
}
