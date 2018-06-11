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
        byte[] byteString = fileName.getBytes();
        String fileNameGDrive = null;
        try {
            fileNameGDrive = new String(byteString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (fileName.contains(" ")) {
            fileName = fileName.replaceAll(" ", "_");
        }

        try {

            ProcessBuilder builder = new ProcessBuilder( "streamlink", "https://www.twitch.tv/" + user,
                    SettingsProperties.getStreamQuality(), "-o", filePath + fileName);
            System.out.println(builder.toString());
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
            System.out.println(line);
            if (line.contains("[cli][info] Stream ended")) {
                System.out.println("output contain stream ended");

                try {
                    GoogleDriveService.upload(fileNameGDrive, filePath);
                } catch (IOException e) {
                    System.err.println("Can't find recorded file" + fileNameGDrive + filePath);
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
