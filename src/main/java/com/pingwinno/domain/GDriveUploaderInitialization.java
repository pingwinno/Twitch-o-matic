package com.pingwinno.domain;

import com.pingwinno.application.StorageHelper;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.google.services.GoogleDriveService;

import java.util.logging.Logger;

public class GDriveUploaderInitialization {
    private static Logger log = Logger.getLogger(GoogleDriveService.class.getName());

    public static void initialize() {
        if (SettingsProperties.getUploadToGDrive().equals("enable")) {
            StorageHelper.initialStorageCheck();
            log.info("Initialize GDrive service");
            GoogleDriveService.createDriveService();
        }
    }
}
