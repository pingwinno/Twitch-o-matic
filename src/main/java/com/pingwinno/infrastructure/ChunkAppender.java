package com.pingwinno.infrastructure;

import java.io.*;
import java.util.List;

public class ChunkAppender {


    public static void copyfile(String srFile, String dtFile){
        try{
            File f2 = new File(srFile);
            File f1 = new File(dtFile);
            InputStream in = new FileInputStream(f1);

            //For Append the file.
            OutputStream out = new FileOutputStream(f2,true);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
        }
        catch(FileNotFoundException ex){
            System.out.println(ex.getMessage() + " in the specified directory.");
            System.exit(0);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    }

