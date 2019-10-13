package net.streamarchive.application.twitch.playlist.handler;

import net.streamarchive.application.DateConverter;
import net.streamarchive.infrastructure.HttpSevice;
import net.streamarchive.infrastructure.StreamNotFoundException;
import net.streamarchive.infrastructure.models.StreamDataModel;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class VodMetadataHelper {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(VodMetadataHelper.class.getName());

    public StreamDataModel getLastVod(long userId) throws IOException, InterruptedException,
            StreamNotFoundException {

        HttpSevice httpSevice = new HttpSevice();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/helix/videos?user_id=" + userId);
        httpGet.addHeader("Client-ID", "eanof9ptu3k9448ukqe85cctiic8gm");
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSevice.getService(httpGet, true).getEntity()));
        StreamDataModel streamMetadata = new StreamDataModel();
        if (jsonObj.getJSONArray("data") != null) {
            streamMetadata = getVodMetadata(Integer.parseInt(jsonObj.getJSONArray("data")
                    .getJSONObject(0).get("id").toString()));
            httpSevice.close();

        }
        return streamMetadata;
        }

    public StreamDataModel getVodMetadata(int vodId) throws IOException,
            InterruptedException, StreamNotFoundException {

        HttpSevice httpSevice = new HttpSevice();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/videos/" + vodId);
        httpGet.addHeader("Client-ID", "eanof9ptu3k9448ukqe85cctiic8gm");
        httpGet.addHeader("Accept","application/vnd.twitchtv.v5+json");
        JSONObject dataObj =
                new JSONObject(EntityUtils.toString(httpSevice.getService(httpGet, true).getEntity()));
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
                } else streamMetadata.setGame("");
                httpSevice.close();
            } catch (IllegalStateException | JSONException e) {
                log.error("{}", e);
                throw new StreamNotFoundException("Stream " + vodId + "not found");
            }
        } else {
            throw new StreamNotFoundException("Stream " + vodId + "not found");
        }
        return streamMetadata;
    }

    public boolean isRecording(int vodId) throws IOException, InterruptedException {

        HttpSevice httpSevice = new HttpSevice();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/videos/" + vodId);
        httpGet.addHeader("Client-ID", "eanof9ptu3k9448ukqe85cctiic8gm");
        httpGet.addHeader("Accept", "application/vnd.twitchtv.v5+json");
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSevice.getService(httpGet, true).getEntity()));
        log.trace("{}", jsonObj);
        httpSevice.close();
        return jsonObj.get("status").toString().equals("recording");
    }

}
