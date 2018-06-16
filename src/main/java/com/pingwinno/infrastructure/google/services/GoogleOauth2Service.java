package com.pingwinno.infrastructure.google.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


public class GoogleOauth2Service {

    private static Logger log = Logger.getLogger(GoogleDriveService.class.getName());
    private static final List<String> SCOPES = Collections.singletonList(
                "https://www.googleapis.com/auth/drive");

    private GoogleOauth2Service(){
            }

            static Credential authorize() throws Exception {
                log.info("Authorization start");
            // load client secrets
                GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GoogleServiceCore.JSON_FACTORY,
                        new InputStreamReader(GoogleOauth2Service.class.getResourceAsStream("/client_secrets.json")));
            // set up authorization code flow
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleServiceCore.HTTP_TRANSPORT, GoogleServiceCore.JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(
                    GoogleServiceCore.DATA_STORE_FACTORY).build();
            // authorize
            return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        }


    }

