package net.streamarchive.application;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class CommandLineExecutor {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(CommandLineExecutor.class.getName());
    private String path;
    String line;
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

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
