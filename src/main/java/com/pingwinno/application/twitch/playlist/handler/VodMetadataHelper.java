package com.pingwinno.application.twitch.playlist.handler;

import com.pingwinno.application.DateConverter;
import com.pingwinno.infrastructure.HttpSeviceHelper;
import com.pingwinno.infrastructure.models.StreamExtendedDataModel;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class VodMetadataHelper {

    public static StreamExtendedDataModel getLastVod(String user) throws IOException, InterruptedException {

        HttpSeviceHelper httpSeviceHelper = new HttpSeviceHelper();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/channels/" + user +
                "/videos?limit=1&broadcast_type=archive&sort=time");
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSeviceHelper.getService(httpGet, true)));
        StreamExtendedDataModel streamMetadata = new StreamExtendedDataModel();
        if (jsonObj.getJSONArray("videos") != null) {
            JSONArray params = jsonObj.getJSONArray("videos");
            JSONObject videoObj = params.getJSONObject(0);
            String rawVodId = videoObj.get("_id").toString();
            //delete "v" from id field
            streamMetadata = getVodMetadata(rawVodId.substring(0, 0) + rawVodId.substring(1));
            httpSeviceHelper.close();

        }
        return streamMetadata;
        }

    public static StreamExtendedDataModel getVodMetadata(String vodId) throws IOException, InterruptedException {

        HttpSeviceHelper httpSeviceHelper = new HttpSeviceHelper();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/videos/" + vodId);
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        httpGet.addHeader("Accept","application/vnd.twitchtv.v5+json");
        JSONObject dataObj =
                new JSONObject(EntityUtils.toString(httpSeviceHelper.getService(httpGet, true)));
        StreamExtendedDataModel streamMetadata = new StreamExtendedDataModel();

        if (!dataObj.toString().equals("")) {
            streamMetadata.setVodId(vodId);
            streamMetadata.setTitle(dataObj.get("title").toString());
            streamMetadata.setDate(DateConverter.convert(dataObj.get("recorded_at").toString()));

            streamMetadata.setPreviewUrl((dataObj.getJSONObject("preview").get("large")).toString());
            if (!dataObj.get("game").toString().equals("")) {
                streamMetadata.setGame(dataObj.get("game").toString());
            }
            else streamMetadata.setGame("");
            httpSeviceHelper.close();
        }
        return streamMetadata;
    }

}
