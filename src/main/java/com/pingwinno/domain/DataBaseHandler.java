package com.pingwinno.domain;

import com.google.gson.Gson;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.models.StreamMetadataModel;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

public class DataBaseHandler {
    private String metadataString;
public DataBaseHandler(StreamMetadataModel streamMetadataModel){
        Gson gson = new Gson();
         metadataString = gson.toJson(streamMetadataModel);
    }

public  void writeToLocalDB() throws IOException {

    FileWriter fstream = new FileWriter(SettingsProperties.getRecordedStreamPath()+"filename.json", true);
    BufferedWriter out = new BufferedWriter(fstream);

    out.write(metadataString);
    out.newLine();

    //close buffer writer
    out.close();
    fstream.close();
}
public  void writeToRemoteDB() throws IOException {
    HttpClient httpClient = HttpClients.createDefault();
    HttpPut request = new HttpPut(SettingsProperties.getRedisPutEndpoint());
    request.setEntity(new StringEntity(metadataString, "utf-8"));
    httpClient.execute(request);
}

}
