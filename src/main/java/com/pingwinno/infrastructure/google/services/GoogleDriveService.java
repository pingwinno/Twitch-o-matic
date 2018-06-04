package com.pingwinno.infrastructure.google.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.pingwinno.infrastructure.SettingsProperties;

import java.io.IOException;
import java.util.Collections;


public class GoogleDriveService {

    private static Drive driveService;

     public static void createDriveService()  {
        Credential credential = null;
        // authorization
         System.out.println("Try start authorize");
         try {
             credential = GoogleOauth2Service.authorize();
         } catch (Exception e) {
             e.printStackTrace();
         }
         System.out.println("Authorization complete");
         // set up global Drive instance
        driveService = new Drive.Builder(GoogleServiceCore.HTTP_TRANSPORT, GoogleServiceCore.JSON_FACTORY, credential).setApplicationName(
                GoogleServiceCore.APPLICATION_NAME).build();
        System.out.println("Credentials saved to " + GoogleServiceCore.DATA_STORE_DIR.getAbsolutePath());
        // success!
    }



   static public void upload(String fileName, java.io.File filePath) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(SettingsProperties.getGooglePhotosPath()));
        FileContent mediaContent = new FileContent("video/mp4", filePath);
        File file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, parents")
                .execute();
        System.out.println("File ID: " + file.getId());
    }


}
