package com.pingwinno.domain;

import com.pingwinno.application.StorageHelper;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.google.services.GoogleDriveService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class StreamlinkRunner {


    public void runStreamlink(String fileName, String filePath, String user) {
        //command line for run streamlink

        // StorageHelper.cleanUpStorage();

        try {

            ProcessBuilder builder = new ProcessBuilder( "streamlink", "https://www.twitch.tv/" + user,
                    SettingsProperties.getStreamQuality(), "-o", filePath + fileName);
            System.out.println(builder.toString());
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = " ";
            while (line != null) {
                line = r.readLine();

                System.out.println(line);
            }

                if (line.contains("[cli][info] Stream ended")) {
                System.out.println("output contain stream ended");



            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
