package net.streamarchive.application.twitch.playlist.handler;

import net.streamarchive.infrastructure.HttpSevice;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

public class UserIdGetter {

    private static Logger log = Logger.getLogger(UserIdGetter.class.getName());

    public static long getUserId(String user) throws IOException, InterruptedException {

        HttpSevice httpSevice = new HttpSevice();
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/helix/users?login=" + user);
        httpGet.addHeader("Client-ID", "s9onp1rs4s93xvfscjfdxui9pracer");
        log.fine(httpGet.toString());
        JSONObject jsonObj =
                new JSONObject(EntityUtils.toString(httpSevice.getService(httpGet, true).getEntity()));
        long userId;
        try {
            JSONArray params = jsonObj.getJSONArray("data");
            JSONObject dataObj = params.getJSONObject(0);
            userId = Long.parseLong(dataObj.get("id").toString());

        } catch (JSONException exception){
            userId = getUserId(user);
        }
        return userId;
    }
}
