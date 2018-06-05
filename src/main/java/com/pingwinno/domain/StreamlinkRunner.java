package com.pingwinno.domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class StreamlinkRunner {

    private boolean isStreamEnded;

    public StreamlinkRunner()
    {
        isStreamEnded = true;
    }

    public void runStreamlink(String fileName,String filePath, String user) {
        //command line for run streamlink
        isStreamEnded = false;
        if (fileName.contains(" ")) {
           fileName = fileName.replaceAll(" ", "\\ ");
        }

        String command = String.join(" ", "streamlink", "https://www.twitch.tv/" + user, "best", "-o", filePath + fileName);
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
                System.out.println(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(output.toString());
        if (output.toString().contains("[cli][info] Stream ended")){
            System.out.println("output contain stream ended");
            isStreamEnded = true;

        }
    }
    public boolean isStreamEnded()
    {
        return isStreamEnded;
    }
}
