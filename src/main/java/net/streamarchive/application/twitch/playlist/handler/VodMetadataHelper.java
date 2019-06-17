package net.streamarchive.application.twitch.playlist.handler;

import net.streamarchive.application.DateConverter;
import net.streamarchive.infrastructure.HttpSevice;
import net.streamarchive.infrastructure.StreamNotFoundExeption;
import net.streamarchive.infrastructure.models.StreamDataModel;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class VodMetadataHelper {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(VodMetadataHelper.class.getName());

    public static StreamDataModel getLastVod(String user) throws IOException, InterruptedException,
            StreamNotFoundExeption {

        HttpSevice httpSevice = new HttpSevice();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/channels/" + user +
                "/videos?limit=1&broadcast_type=archive&sort=time");
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSevice.getService(httpGet, true).getEntity()));
        StreamDataModel streamMetadata = new StreamDataModel();
        if (jsonObj.getJSONArray("videos") != null) {
            JSONArray params = jsonObj.getJSONArray("videos");
            JSONObject videoObj = params.getJSONObject(0);
            String rawVodId = videoObj.get("_id").toString();
            //delete "v" from id field
            streamMetadata = getVodMetadata(Integer.valueOf(rawVodId.substring(0, 0) + rawVodId.substring(1)));
            httpSevice.close();

        }
        return streamMetadata;
        }

    public static StreamDataModel getVodMetadata(int vodId) throws IOException,
            InterruptedException, StreamNotFoundExeption {

        HttpSevice httpSevice = new HttpSevice();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/videos/" + vodId);
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        httpGet.addHeader("Accept","application/vnd.twitchtv.v5+json");
        JSONObject dataObj =
                new JSONObject(EntityUtils.toString(httpSevice.getService(httpGet, true).getEntity()));
        StreamDataModel streamMetadata = new StreamDataModel();
        log.trace("{}", dataObj);
        if (!dataObj.toString().equals("")) {
            try {
                streamMetadata.setVodId(vodId);
                streamMetadata.setTitle(dataObj.get("title").toString());
                streamMetadata.setUser(dataObj.getJSONObject("channel").get("name").toString());
                streamMetadata.setDate(DateConverter.convert(dataObj.get("recorded_at").toString()));

                streamMetadata.setPreviewUrl((dataObj.getJSONObject("preview").get("large")).toString());
                if (!dataObj.get("game").toString().equals("")) {
                    streamMetadata.setGame(dataObj.get("game").toString());
                } else streamMetadata.setGame("");
                httpSevice.close();
            } catch (IllegalStateException | JSONException e) {
                log.error("{}", e);
                throw new StreamNotFoundExeption("Stream " + vodId + "not found");
            }
        } else {
            throw new StreamNotFoundExeption("Stream " + vodId + "not found");
        }
        return streamMetadata;
    }

}
