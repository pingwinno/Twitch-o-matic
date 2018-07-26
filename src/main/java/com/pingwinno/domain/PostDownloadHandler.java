package com.pingwinno.domain;

import com.pingwinno.application.StorageHelper;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.google.services.GoogleCloudStorageService;
import com.pingwinno.infrastructure.google.services.GoogleDriveService;

import java.io.IOException;

public class PostDownloadHandler {
    public static void handleDownloadedStream(String streamFileName) throws IOException {
        String streamFilePath = SettingsProperties.getRecordedStreamPath() + streamFileName;
        if (SettingsProperties.getUploadToCloudStorage().equals("true")) {
            GoogleDriveService.upload(streamFilePath, streamFileName);
        }
        if (SettingsProperties.getUploadToGDrive().equals("true")) {
            GoogleCloudStorageService.upload(streamFilePath, streamFileName);
        }
        if (SettingsProperties.getDeleteFileAfterUpload().equals("true")) {
            StorageHelper.deleteUploadedFile(streamFilePath);
        }
    }
}
