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
    private int failsCount = 0;

    public HttpEntity getService(HttpGet httpGet, boolean sslVerifyEnable) throws IOException {
        if (sslVerifyEnable) {
            client = HttpClients.createDefault();
        } else {
            //ignore SSL because on usher.twitch.tv its broken
            client = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        }
        try {
            response = client.execute(httpGet);
        } catch (IOException e) {
            if (failsCount < 6) {
                this.failsCount++;
                log.warn("request failed. Retry...");
                this.getService(httpGet, sslVerifyEnable);
                log.trace("Request response: {}", response.getStatusLine());
            } else {
                log.error("Request failed more than 5 times. Close connection");
                throw new IOException();
            }
        }
        return response.getEntity();

    }

    public HttpEntity getService(HttpPost httpPost, boolean sslVerifyEnable) throws IOException {

        if (sslVerifyEnable) {
            client = HttpClients.createDefault();
        } else {
            //ignore SSL because on usher.twitch.tv its broken
            client = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        }
        try {
            response = client.execute(httpPost);
        } catch (IOException e) {
            if (failsCount < 6) {
                this.failsCount++;
                log.warn("request failed. Retry...");
                this.getService(httpPost, sslVerifyEnable);
                log.trace("Request response: {}", response.getStatusLine());
            } else {
                log.error("Request failed more than 5 times. Close connection");
                throw new IOException();
            }
        }
        return response.getEntity();

    }

    public void close() throws IOException {
        client.close();
        response.close();
    }
}
