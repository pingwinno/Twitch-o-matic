package com.pingwinno.infrastructure;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.logging.Logger;

public class HttpSeviceHelper {

    private static Logger log = Logger.getLogger(HttpSeviceHelper.class.getName());
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
        log.fine("Request response: " + response.getStatusLine());
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_NOT_FOUND) {
            while (response.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                response = client.execute(httpGet);
                Thread.sleep(10000);
            }
        }
        else {
            response = null;
        }
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
        log.fine("Request response: " + response.getStatusLine());
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_NOT_FOUND) {
            while (response.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                response = client.execute(httpPost);
                Thread.sleep(10000);
            }
        }
        else {
            response = null;
        }
        return response.getEntity();
    }

    public void close() throws IOException {
        client.close();
        response.close();
    }
}
