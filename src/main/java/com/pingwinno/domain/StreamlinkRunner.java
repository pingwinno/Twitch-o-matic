package com.pingwinno.domain;

import com.pingwinno.infrastructure.SettingsProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamlinkRunner {

    private static Logger log = Logger.getLogger(StreamlinkRunner.class.getName());

    public void runStreamlink(String fileName, String filePath, String user) {
        //command line for run streamlink

        // StorageHelper.cleanUpStorage();

        try {

            ProcessBuilder builder = new ProcessBuilder("streamlink", "https://www.twitch.tv/" + user,
                    SettingsProperties.getStreamQuality(), "-o", filePath + fileName);
            System.out.println(builder.toString());
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = " ";
                 while (line != null){
                line = r.readLine();
                log.info(line);
                }
            } catch (IOException e) {
            log.log(Level.SEVERE, "Exception: ", e);
        }

    }
}
