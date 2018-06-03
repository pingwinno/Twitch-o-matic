package com.pingwinno.domain;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

public class GoogleDriveUploader {
    public void upload(String token) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("https://www.googleapis.com/upload/drive/v3/files?uploadType=media");
        File file = new File("config.prop");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("upfile", file, ContentType.DEFAULT_BINARY, "config.prop");
        //
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        post.setHeader("Content-Type","text/plain");
        post.setHeader("Authorization", "Bearer "+token);
        try {
            HttpResponse response = client.execute(post);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
