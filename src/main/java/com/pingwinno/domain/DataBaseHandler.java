package com.pingwinno.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamDataModel;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.LoggerFactory;

import java.io.*;

public class DataBaseHandler {
    private String metadataString;
    private org.slf4j.Logger log = LoggerFactory.getLogger(getClass().getName());

    public DataBaseHandler(StreamDataModel streamDataModel) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        metadataString = mapper.writeValueAsString(streamDataModel);
        log.trace("{}", metadataString);
    }

    public void writeToLocalDB() throws IOException {

        FileWriter fstream = new FileWriter(SettingsProperties.getRecordedStreamPath() + "filename.json", true);
        BufferedWriter out = new BufferedWriter(fstream);
        log.debug("Write to local db...");
        out.write(metadataString);
        out.newLine();
        //close buffer writer
        out.close();
        fstream.close();
        log.debug("done");
    }

    public void writeToRemoteDB() throws IOException {
        log.debug("Write to remote db...");
        HttpClient httpClient = HttpClients.createDefault();
        log.trace("Remote db endpoint: {}", SettingsProperties.getRedisPutEndpoint());
        HttpPut request = new HttpPut(SettingsProperties.getRedisPutEndpoint());
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.setEntity(new StringEntity(metadataString, "utf-8"));
        HttpResponse response = httpClient.execute(request);
        log.debug("Server respomse: {}", response.getStatusLine().toString());
        log.trace("{}", new BufferedReader(new InputStreamReader(response.getEntity().getContent())));
    }

}
