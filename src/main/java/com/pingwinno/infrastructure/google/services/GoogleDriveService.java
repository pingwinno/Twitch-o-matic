package com.pingwinno.domain;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;

import java.util.Collections;


public class GoogleDriveService {







    private File setFolder() {
        String folderId = "0BwwA4oUTeiV1TGRPeTVjaWRDY1E";
        File fileMetadata = new File();
        fileMetadata.setName("photo.jpg");
        fileMetadata.setParents(Collections.singletonList(folderId));
        java.io.File filePath = new java.io.File("files/photo.jpg");
        FileContent mediaContent = new FileContent("image/jpeg", filePath);
        File file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, parents")
                .execute();
        System.out.println("File ID: " + file.getId());
    }

    public void upload(String token,String folderId) {
        File fileMetadata = new File();
        fileMetadata.setName("photo.jpg");
        java.io.File filePath = new java.io.File("files/photo.jpg");
        FileContent mediaContent = new FileContent("image/jpeg", filePath);
        File file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        System.out.println("File ID: " + file.getId());
    }
}
