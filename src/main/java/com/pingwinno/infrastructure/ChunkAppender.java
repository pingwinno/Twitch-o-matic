package com.pingwinno.infrastructure;

import java.io.*;
import java.util.logging.Logger;

public class ChunkAppender {

    private static Logger log = Logger.getLogger(ChunkAppender.class.getName());

    public static void copyfile(String destinationFile, InputStream in) {
        try {
            File file1 = new File(destinationFile);


            //For Append the file.
            OutputStream out = new FileOutputStream(file1, true);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            log.fine("File copied.");
        } catch (FileNotFoundException e) {
            log.severe(e.getMessage() + " in the specified directory.");
            System.exit(0);
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

}

