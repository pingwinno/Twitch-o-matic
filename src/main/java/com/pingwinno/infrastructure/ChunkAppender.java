package com.pingwinno.infrastructure;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChunkAppender {

    private static Logger log = Logger.getLogger(ChunkAppender.class.getName());

    public static void copyfile(String srFile, String dtFile) {
        try {
            File f2 = new File(srFile);
            File f1 = new File(dtFile);
            InputStream in = new FileInputStream(f1);

            //For Append the file.
            OutputStream out = new FileOutputStream(f2, true);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            f2.delete();
            in.close();
            out.close();
            log.info("File copied.");
        } catch (FileNotFoundException ex) {
            log.log(Level.SEVERE, ex.getMessage() + " in the specified directory.");
            System.exit(0);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

}

