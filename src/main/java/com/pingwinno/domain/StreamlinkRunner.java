package com.pingwinno.domain;

import com.pingwinno.application.StreamFileNameHelper;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.google.services.GoogleDriveService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamlinkRunner {

    private static Logger log = Logger.getLogger(StreamlinkRunner.class.getName());

    public static void runStreamlink(String streamFileName) {
        //command line for run streamlink

        //

        try {

            ProcessBuilder builder = new ProcessBuilder("streamlink", "https://www.twitch.tv/" + SettingsProperties.getUser(),
                    SettingsProperties.getStreamQuality(), "-o", streamFileName);
            System.out.println(builder.toString());
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = " ";
                 while (line != null){
                line = r.readLine();
                log.info(line);
                }
            GoogleDriveService.upload(SettingsProperties.getRecordedStreamPath(), streamFileName);
            } catch (IOException e) {
            log.log(Level.SEVERE, "Exception: ", e);
        }


    }
}
