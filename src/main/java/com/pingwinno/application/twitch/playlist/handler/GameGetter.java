package com.pingwinno.application.twitch.playlist.handler;

import com.pingwinno.infrastructure.HttpSeviceHelper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

public class GameGetter {

    private static Logger log = Logger.getLogger(GameGetter.class.getName());

    public static String getUserId(String gameID) throws IOException, InterruptedException {

        HttpSeviceHelper httpSeviceHelper = new HttpSeviceHelper();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/helix/games?id=" + gameID);
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        log.fine(httpGet.toString());
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSeviceHelper.getService(httpGet, true)));
        String gameName;
        try {

            JSONArray params = jsonObj.getJSONArray("data");
            JSONObject dataObj = params.getJSONObject(0);
            gameName = dataObj.get("name").toString();
        }
        catch (JSONException e){
            log.info("Game is empty");
            gameName = "empty";
        }

        return gameName;
    }
}
