package com.pingwinno.infrastructure;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SettingsProperties {
    private static Logger log = Logger.getLogger(SettingsProperties.class.getName());
    
    private final static String PROPSFILE = "config.prop";

    private static Properties props;

    private static Properties getProperties() throws IOException {
        try {

            if (props == null) {
                props = new Properties();
                props.load(new FileInputStream(new File(PROPSFILE)));
            }
        } catch (FileNotFoundException e) {
            log.log(Level.SEVERE,"config.prop not found");
            log.log(Level.SEVERE,"Place config.prop in Twitch-o-matic folder and try again");
            System.exit(1);
        }
        return props;
    }

    public static String getCallbackAddress() {
        String callbackAddress = null;
        try {
            callbackAddress = getProperties().getProperty("CallbackAddress");
        } catch (IOException e) {
            log.log(Level.SEVERE,"Can't read CallbackAddress");
            System.exit(1);
        }
        return callbackAddress;
    }

    public static int getServerPort() {
        int serverPort = 0;
        try {
            serverPort = Integer.parseInt(getProperties().getProperty("ServerPort"));
        } catch (IOException e) {
            log.log(Level.SEVERE,"Can't read ServerPort");
            System.exit(1);
        }
        return serverPort;
    }

    public static String getUser() {
        String user = null;
        try {
            user = getProperties().getProperty("User");
        } catch (IOException e) {
            log.log(Level.SEVERE,"Can't read User");
            System.exit(1);
        }
        return user;
    }

    public static String getGooglePhotosPath() {
        String googlePhotosPath = null;
        try {
            googlePhotosPath = getProperties().getProperty("GooglePhotosPath");
        } catch (IOException e) {
            log.log(Level.SEVERE,"Can't read GooglePhotosPath");
            System.exit(1);
        }
        return googlePhotosPath;
    }

    public static String getRecordedStreamPath() {
        String recordedStreamPath = null;
        try {
            recordedStreamPath = getProperties().getProperty("RecordedStreamPath");
        } catch (IOException e) {
            log.log(Level.SEVERE,"Can't read RecordedStreamPath");
            System.exit(1);
        }
        return recordedStreamPath;
    }

    public static String getRecordMachineName() {
        String recordMachineName = null;
        try {
            recordMachineName = getProperties().getProperty("RecordMachineName");
        } catch (IOException e) {
            log.log(Level.SEVERE,"Can't read RecordMachineName");
            System.exit(1);
        }
        return recordMachineName;
    }

    public static String getIgnoreStorageCheck() {
        String ignoreStorageCheck = null;
        try {
            ignoreStorageCheck = getProperties().getProperty("IgnoreStorageCheck");
        } catch (IOException e) {
            log.log(Level.SEVERE,"Can't read IgnoreStorageCheck");
            System.exit(1);
        }
        return ignoreStorageCheck;
    }

    public static String getStreamQuality() {
        String streamQuality = null;
        try {
            streamQuality = getProperties().getProperty("StreamQuality");
        } catch (IOException e) {
            log.log(Level.SEVERE,"Can't read StreamQuality");
            System.exit(1);
        }
        return streamQuality;
    }
    public static String getDownloadMode() {
        String downloadMode = null;
        try {
            downloadMode = getProperties().getProperty("DownloadMode");
        } catch (IOException e) {
            log.log(Level.SEVERE,"Can't read DownloadMode");
            System.exit(1);
        }
        return downloadMode;
    }
}





