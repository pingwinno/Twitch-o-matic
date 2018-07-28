package com.pingwinno.application;

import com.pingwinno.domain.StreamlinkRunner;
import com.pingwinno.domain.VodDownloader;
import com.pingwinno.infrastructure.SettingsProperties;

import java.util.logging.Logger;

public class DownloaderSelector {
    private static Logger log = Logger.getLogger(DownloaderSelector.class.getName());

    public static void runDownloader(String streamName) {

        log.fine("initialize downloader");
        if (SettingsProperties.getDownloadMode().equals("vod")) {
            log.fine("Running vod downloader");
            VodDownloader vodDownloader = new VodDownloader();
            vodDownloader.initializeDownload(streamName);
        } else if (SettingsProperties.getDownloadMode().equals("streamlink")) {
            log.fine("Running streamlink downloader");
            StreamlinkRunner.runStreamlink(streamName);
        }
    }
}
