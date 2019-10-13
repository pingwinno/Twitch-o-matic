package net.streamarchive.application.twitch.playlist.handler;

import net.streamarchive.infrastructure.HttpSevice;
import net.streamarchive.infrastructure.StreamNotFoundException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
@Scope("prototype")
public class MasterPlaylistDownloader {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(MasterPlaylistDownloader.class.getName());

    public String getPlaylist(String user, String vodId, String quality) throws IOException, InterruptedException, URISyntaxException, StreamNotFoundException {

        HttpSevice httpSevice = new HttpSevice();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/videos/" + vodId);
        httpGet.addHeader("Client-ID", "eanof9ptu3k9448ukqe85cctiic8gm");
        httpGet.addHeader("Accept", "application/vnd.twitchtv.v5+json");
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSevice.getService(httpGet, true).getEntity()));
        String previewUrl = null;

        previewUrl = jsonObj.get("animated_preview_url").toString();


        if (previewUrl != null) {
            URI url = new URI(previewUrl);
            return "https://vod-secure.twitch.tv" +
                    url.getPath().substring(0, url.getPath().indexOf("/", 1)) + "/" + quality + "/index-dvr.m3u8";

        }
        if (httpSevice.getService(httpGet, true).getStatusLine().getStatusCode() == 404) {
            throw new StreamNotFoundException("Stream " + vodId + " not found");
        }
        throw new IOException("Can't get substream link");

    }
}
