package com.pingwinno.infrastructure.google.services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;

class GoogleServiceCore {

    static final String APPLICATION_NAME = "Twitch-o-matic_test";
    static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("datastore"), ".store/oauth");
    static FileDataStoreFactory DATA_STORE_FACTORY;
    static HttpTransport HTTP_TRANSPORT;
    static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    static {
        try {
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();

        }
    }
}
