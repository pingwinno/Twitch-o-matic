package net.streamarchive.application.twitch.playlist.handler;


import net.streamarchive.infrastructure.StreamNotFoundException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class MasterPlaylistDownloader {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(MasterPlaylistDownloader.class.getName());

    @Autowired
    RestTemplate restTemplate;

    public String getPlaylist(String vodId, String quality) throws IOException, InterruptedException, URISyntaxException, StreamNotFoundException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Client-ID", "eanof9ptu3k9448ukqe85cctiic8gm");
        httpHeaders.add("Accept", "application/vnd.twitchtv.v5+json");
        HttpEntity<String> requestEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://api.twitch.tv/kraken/videos/" + vodId,
                HttpMethod.GET, requestEntity, String.class);
        JSONObject jsonObj =
                new JSONObject(responseEntity.getBody());
        String previewUrl = null;

        previewUrl = jsonObj.get("animated_preview_url").toString();


        if (previewUrl != null) {
            URI url = new URI(previewUrl);
            return "https://vod-secure.twitch.tv" +
                    url.getPath().substring(0, url.getPath().indexOf("/", 1)) + "/" + quality + "/index-dvr.m3u8";

        }
        if (responseEntity.getStatusCode().value() == 404) {
            throw new StreamNotFoundException("Stream " + vodId + " not found");
        }
        throw new IOException("Can't get substream link");

    }
}
