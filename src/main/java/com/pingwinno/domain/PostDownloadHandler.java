package com.pingwinno.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.infrastructure.SettingsProperties;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostDownloadHandler {

    private static Logger log = Logger.getLogger(PostDownloadHandler.class.getName());

    public static void handleDownloadedStream() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        String[] command = mapper.readValue(SettingsProperties.getCommandArgs(), String[].class);

        try {
            ProcessBuilder builder = new ProcessBuilder(command);

            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = " ";
            while (line != null) {
                line = r.readLine();
                log.info(line);
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Can't run command. Exception: " + e.toString(), e);
        }
    }

}
