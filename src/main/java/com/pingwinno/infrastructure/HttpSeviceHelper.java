package com.pingwinno.infrastructure;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpSeviceHelper {

    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());
    private CloseableHttpClient client;
    private CloseableHttpResponse response;

    public HttpEntity getService(HttpGet httpGet, boolean sslVerifyEnable) throws IOException, InterruptedException {

        if (sslVerifyEnable) {
            client = HttpClients.createDefault();
        } else {
            //ignore SSL because on usher.twitch.tv its broken
            client = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        }
        response = client.execute(httpGet);
        //System.out.println(EntityUtils.toString(response.getEntity()));
        log.debug("Request response: {}", response.getStatusLine());

        return response.getEntity();
    }

    public HttpEntity getService(HttpPost httpPost, boolean sslVerifyEnable) throws IOException, InterruptedException {

        if (sslVerifyEnable) {
            client = HttpClients.createDefault();
        } else {
            //ignore SSL because on usher.twitch.tv its broken
            client = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        }
        response = client.execute(httpPost);
        log.debug("Request response: {}", response.getStatusLine());

        return response.getEntity();
    }

    public void close() throws IOException {
        client.close();
        response.close();
    }
}
