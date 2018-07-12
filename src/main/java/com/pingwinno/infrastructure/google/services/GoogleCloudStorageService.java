package com.pingwinno.infrastructure.google.services;

import com.google.cloud.storage.*;
import com.pingwinno.infrastructure.SettingsProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GoogleCloudStorageService {


    public static void upload(String filePath, String fileName) throws IOException {
        if (SettingsProperties.getUploadToCloudStorage().equals("true")) {
            Storage storage = StorageOptions.getDefaultInstance().getService();
            String bucketName = "olyashaa_streams_storage";
            Path path = Paths.get(filePath );
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("video/mp4").build();
            Blob blob = storage.create(blobInfo, Files.readAllBytes(path));
        }
    }
}
