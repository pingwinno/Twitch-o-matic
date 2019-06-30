package net.streamarchive.application.twitch.playlist.handler;

import net.streamarchive.infrastructure.HttpSevice;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RecordStatusGetter {
    public boolean isRecording(int vodId) throws IOException, InterruptedException {

        HttpSevice httpSevice = new HttpSevice();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/videos/" + vodId);
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSevice.getService(httpGet, true).getEntity()));

        httpSevice.close();
        return jsonObj.get("status").toString().equals("recording");
    }
}
