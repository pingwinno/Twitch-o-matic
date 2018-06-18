package com.pingwinno.application;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlaylistGetter {


    public static void getPlaylistToken(String user) throws IOException, NoSuchAlgorithmException, URISyntaxException {


        CloseableHttpClient client = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        //ignore SSL because on usher.twitch.tv its broken
        HttpGet httpGet = new HttpGet("https://api.twitch.tv/api/vods/273669368/access_token");
        httpGet.addHeader("Client-ID", "4zswqk0crwt2wy4b76aaltk2z02m67");
        System.out.println(httpGet.toString());
        CloseableHttpResponse response = client.execute(httpGet);


        System.out.println(response.getStatusLine());

        JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
        System.out.println(json.toString());
        URIBuilder builder = new URIBuilder("https://usher.twitch.tv/vod/273669368");
        builder.setParameter("player", "twitchweb");
        builder.setParameter("nauth", json.get("token").toString());
        builder.setParameter("nauthsig", json.get("sig").toString());
        builder.setParameter("allow_audio_only", "true");
        builder.setParameter("allow_source", "true");
        builder.setParameter("type", "any");

        int randomNum = ThreadLocalRandom.current().nextInt(1, 99998);
        builder.setParameter("p", Integer.toString(randomNum));

        httpGet = new HttpGet(builder.build());





        String urlencoded = builder.toString();
        System.out.println(urlencoded);
        response = client.execute(httpGet);


        String entity = response.getEntity().getContent().toString();
        System.out.println(response.getStatusLine());
        System.out.println(entity);

        BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
        String filePath = "playlist.m3u8";
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
        int inByte;
        while((inByte = bis.read()) != -1) bos.write(inByte);
        bis.close();
        bos.close();


        client.close();
        response.close();


    }

}
