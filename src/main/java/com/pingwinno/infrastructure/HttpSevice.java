package com.pingwinno.infrastructure;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class HttpSevice {


    public HttpEntity getService(HttpGet httpGet, boolean sslVerifyEnable) throws IOException {

        CloseableHttpClient client;

        if (sslVerifyEnable == true) {
            client = HttpClients.createDefault();
        }
        else{
            client = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        }

        CloseableHttpResponse response = client.execute(httpGet);
        HttpEntity httpEntity = response.getEntity();
        client.close();
        response.close();
        return httpEntity;
    }
}
