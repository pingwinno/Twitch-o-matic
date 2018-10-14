package com.pingwinno.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingwinno.infrastructure.SettingsProperties;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PostDownloadHandler {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(PostDownloadHandler.class.getName());

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
            log.error("Can't run command. Exception: {}", e);
        }
    }

}
