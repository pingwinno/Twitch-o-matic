package com.pingwinno.notification.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineRunner {


    public void executeCommand(String name, String time) throws InterruptedException, IOException {
        //command line for run streamlink
        String fileName = name +"_"+ time + ".mp4";
        String command = String.join(" ","streamlink","https://www.twitch.tv/olyashaa", "best",  "-o", fileName);
        StringBuffer output = new StringBuffer();

        Process p;
            System.out.println("Streamlink start");
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            try (BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));){

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
            }

        System.out.println(output.toString());

    }

}
