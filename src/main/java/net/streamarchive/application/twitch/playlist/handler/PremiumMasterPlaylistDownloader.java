package net.streamarchive.application.twitch.playlist.handler;

import net.streamarchive.infrastructure.HttpSevice;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
@Scope("prototype")
public class PremiumMasterPlaylistDownloader {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(PremiumMasterPlaylistDownloader.class.getName());

    public String getPlaylist(String user, String vodId, String quality) throws IOException, InterruptedException, URISyntaxException {

        HttpSevice httpSevice = new HttpSevice();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/channels/" + user +
                "/videos?limit=100&broadcast_type=archive&sort=time");
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSevice.getService(httpGet, true).getEntity()));
        String previewUrl = null;
        for (int i = 0; i < jsonObj.getJSONArray("videos").length(); i++) {
            log.debug("element {}", i);
            JSONArray params = jsonObj.getJSONArray("videos");
            JSONObject videoObj = params.getJSONObject(i);
            log.debug("video {}", videoObj.toString());
            String rawVodId = videoObj.get("_id").toString();
            //delete "v" from id field
            if (rawVodId.contains(vodId)) {
                previewUrl = jsonObj.getJSONArray("videos").getJSONObject(i).get("animated_preview_url").toString();
                break;
            }

        }
        if (previewUrl != null) {
            URI url = new URI(previewUrl);
            return "https://vod-secure.twitch.tv" +
                    url.getPath().substring(0, url.getPath().indexOf("/", 1)) + "/" + quality + "/index-dvr.m3u8";

        }
        throw new IOException("Can't get substream link");

    }
}
