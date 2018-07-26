package com.pingwinno.infrastructure.google.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.pingwinno.infrastructure.SettingsProperties;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;


public class GoogleDriveService {

    private static Logger log = Logger.getLogger(GoogleDriveService.class.getName());
    private static Drive driveService;

    public static void createDriveService() {
        Credential credential = null;
        // authorization
        log.info("Try authorize GoogleDriveService");
        try {
            credential = GoogleOauth2Service.authorize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Authorization complete");
        // set up global Drive instance
        driveService = new Drive.Builder(GoogleServiceCore.HTTP_TRANSPORT, GoogleServiceCore.JSON_FACTORY, credential).setApplicationName(
                GoogleServiceCore.APPLICATION_NAME).build();
        log.info("Credentials saved to " + GoogleServiceCore.DATA_STORE_DIR.getAbsolutePath());
        // success!
    }

    static public void upload(String filePath, String fileName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(fileName);

        java.io.File filePathFile = new java.io.File(filePath);
        fileMetadata.setParents(Collections.singletonList(SettingsProperties.getGoogleDrivePath()));
        FileContent mediaContent = new FileContent("video/mp4", filePathFile);
        log.info("Uploading file...");
        File file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, parents")
                .execute();
        System.out.println("File ID: " + file.getId());
    }


}
