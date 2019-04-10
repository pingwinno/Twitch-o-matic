package com.pingwinno.infrastructure;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpSevice {

    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());
    private CloseableHttpClient client;
    private CloseableHttpResponse response;
    private int failsCount = 0;

    public CloseableHttpResponse getService(HttpGet httpGet, boolean sslVerifyEnable) throws IOException, InterruptedException {
        if (sslVerifyEnable) {
            client = HttpClients.createDefault();
        } else {
            //ignore SSL because on usher.twitch.tv its broken
            client = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        }
        try {
            response = client.execute(httpGet);
            log.trace("Request response: {}. Called by {}:{}", response.getStatusLine(),
                    Thread.currentThread().getStackTrace()[2].getClassName(),
                    Thread.currentThread().getStackTrace()[2].getMethodName());
        } catch (IOException e) {
            if (failsCount < 6) {
                this.failsCount++;
                log.warn("request failed. Retry...");
                Thread.sleep(5000);
                this.getService(httpGet, sslVerifyEnable);
                log.trace("Request response: {}. Called by {}:{}", response.getStatusLine(),
                        Thread.currentThread().getStackTrace()[2].getClassName(),
                        Thread.currentThread().getStackTrace()[2].getMethodName());
            } else {
                log.error("Request failed more than 5 times. Close connection");
                throw new IOException();
            }
        }
        return response;

    }

    public CloseableHttpResponse getService(HttpPost httpPost, boolean sslVerifyEnable) throws IOException, InterruptedException {

        if (sslVerifyEnable) {
            client = HttpClients.createDefault();
        } else {
            //ignore SSL because on usher.twitch.tv its broken
            client = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        }
        try {
            response = client.execute(httpPost);
            log.trace("Request response: {}. Called by {}:{}", response.getStatusLine(),
                    Thread.currentThread().getStackTrace()[2].getClassName(),
                    Thread.currentThread().getStackTrace()[2].getMethodName());

        } catch (IOException e) {
            if (failsCount < 6) {
                this.failsCount++;
                log.warn("request failed. Retry...");
                Thread.sleep(5000);
                this.getService(httpPost, sslVerifyEnable);

                log.trace("Request response: {}. Called by {}:{}", response.getStatusLine(),
                        Thread.currentThread().getStackTrace()[2].getClassName(),
                        Thread.currentThread().getStackTrace()[2].getMethodName());
            } else {
                log.error("Request failed more than 5 times. Close connection");
                throw new IOException();
            }
        }
        return response;

    }

    public void close() throws IOException {
        client.close();
        response.close();
    }
}
