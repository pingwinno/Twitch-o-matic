package net.streamarchive.application.postprocessing;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
@Builder
@Slf4j
public class CommandLineExecutor {

    private final String path;
    private String line;

    public void execute(String... command) {

        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            if (path != null) {
                builder.directory(new File(path));
            }
            builder.redirectErrorStream(true);
            Process p = builder.start();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                 BufferedWriter out = new BufferedWriter(new FileWriter(path + "/ffmpeg.log", true))) {
                while ((line = r.readLine()) != null) {
                    out.write(line);
                    out.newLine();
                }
                out.flush();

            }
        } catch (IOException e) {
            log.error("Can't run command. Exception: ", e);
        }
    }


}
