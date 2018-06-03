package com.pingwinno.domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandLineRunner {

    public void executeCommand(String time, String user) {
        //command line for run streamlink
        String fileName = time + ".mp4";
        String command = String.join(" ", "streamlink", "https://www.twitch.tv/" + user, "best", "-o", fileName);
        StringBuilder output = new StringBuilder();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(output.toString());
    }
}
