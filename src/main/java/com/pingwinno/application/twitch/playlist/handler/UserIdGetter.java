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
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/helix/users/" + user);
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        log.info(httpGet.toString());
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSeviceHelper.getService(httpGet, true)));
        return jsonObj.get("_id").toString();
    }
}
