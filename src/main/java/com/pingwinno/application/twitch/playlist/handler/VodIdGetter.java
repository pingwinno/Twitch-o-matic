package com.pingwinno.application.twitch.playlist.handler;

import com.pingwinno.infrastructure.SettingsProperties;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

public class VodIdGetter {
    private static Logger log = Logger.getLogger(VodIdGetter.class.getName());
    public static String getVodId() throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/channels/" + SettingsProperties.getUser() +
                "/videos?limit=1&broadcast_type=archive&sort=time");
        httpGet.addHeader("Client-ID", "4zswqk0crwt2wy4b76aaltk2z02m67");
        log.info(httpGet.toString());
        CloseableHttpResponse response = client.execute(httpGet);
        JSONObject jsonObj = new JSONObject(EntityUtils.toString(response.getEntity()));
        System.out.println(jsonObj.toString());
        JSONArray params = jsonObj.getJSONArray("videos");
        JSONObject videoObj = params.getJSONObject(0);
        System.out.println(jsonObj.toString());
        String str = videoObj.get("_id").toString();
        //delete "v" from id field
        String result = str.substring(0, 0) + str.substring(0+1);
System.out.println(result);
        client.close();
        response.close();

        return result;
    }

    public static boolean getRecordStatus() throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/channels/" + SettingsProperties.getUser() +
                "/videos?limit=1&broadcast_type=archive&sort=time");
        httpGet.addHeader("Client-ID", "4zswqk0crwt2wy4b76aaltk2z02m67");
        log.info(httpGet.toString());
        CloseableHttpResponse response = client.execute(httpGet);
        JSONObject jsonObj = new JSONObject(EntityUtils.toString(response.getEntity()));
        System.out.println(jsonObj.toString());
        JSONArray params = jsonObj.getJSONArray("videos");
        JSONObject videoObj = params.getJSONObject(0);
        System.out.println(jsonObj.toString());
        String status = videoObj.get("status").toString();
        //delete "v" from id field

        client.close();
        response.close();

        return status.equals("recording");
    }

}
