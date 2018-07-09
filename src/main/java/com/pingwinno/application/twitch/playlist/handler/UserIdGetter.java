package com.pingwinno.application.twitch.playlist.handler;

import com.pingwinno.infrastructure.HttpSeviceHelper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

public class UserIdGetter {

    private static Logger log = Logger.getLogger(UserIdGetter.class.getName());

    public static String getUserId(String user) throws IOException {

        HttpSeviceHelper httpSeviceHelper = new HttpSeviceHelper();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/kraken/users/" + user);
        httpGet.addHeader("Client-ID", "4zswqk0crwt2wy4b76aaltk2z02m67");
        log.info(httpGet.toString());
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSeviceHelper.getService(httpGet, true)));
        return jsonObj.get("_id").toString();
    }
}
