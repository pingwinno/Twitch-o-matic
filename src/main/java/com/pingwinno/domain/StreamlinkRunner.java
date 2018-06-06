package com.pingwinno.domain;

import com.pingwinno.application.StorageHelper;
import com.pingwinno.infrastructure.SettingsProperties;
import com.pingwinno.infrastructure.google.services.GoogleDriveService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StreamlinkRunner {


    public void runStreamlink(String fileName, String filePath, String user) {
        //command line for run streamlink
        StorageHelper.cleanUpStorage();
        String fileNameGDrive = fileName;
        if (fileName.contains(" ")) {
            fileName = fileName.replaceAll(" ", "\\ ");
        }

        StringBuilder output = new StringBuilder();
        Process p;
        try {

            String command = String.join(" ", "streamlink", "https://www.twitch.tv/" + user,
                    SettingsProperties.getStreamQuality(), "-o", filePath + fileName);
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                System.out.println(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(output.toString());
        if (output.toString().contains("[cli][info] Stream ended")) {
            System.out.println("output contain stream ended");

            try {
                GoogleDriveService.upload(fileNameGDrive, filePath);
            } catch (IOException e) {
                System.err.println("Can't find recorded file" + fileNameGDrive + filePath);
                e.printStackTrace();
            }

        }
    }

}
