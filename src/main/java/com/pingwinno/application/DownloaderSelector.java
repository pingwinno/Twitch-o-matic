package com.pingwinno.application;

import com.pingwinno.domain.StreamlinkRunner;
import com.pingwinno.domain.VodDownloader;
import com.pingwinno.infrastructure.SettingsProperties;

import java.util.logging.Logger;

public class DownloaderSelector {
    private static Logger log = Logger.getLogger(DownloaderSelector.class.getName());

    public static void runDownloader(String streamName) {

        log.info("initialize downloader");
        if (SettingsProperties.getDownloadMode().equals("vod")) {
            log.info("Running vod downloader");
            VodDownloader vodDownloader = new VodDownloader();
            vodDownloader.initializeDownload(streamName);
        } else if (SettingsProperties.getDownloadMode().equals("streamlink")) {
            log.info("Running streamlink downloader");
            StreamlinkRunner.runStreamlink(StreamFileNameHelper.makeFileName(streamName));
        }
    }
}
