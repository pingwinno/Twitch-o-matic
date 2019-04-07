package com.pingwinno.infrastructure;


import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SettingsProperties {
    private final static String PROPSFILE = "/etc/tom/config.prop";
    private final static String TESTPROPSFILE = "config_test.prop";

    private static org.slf4j.Logger log = LoggerFactory.getLogger(SettingsProperties.class.getName());

    private static Properties props;

    private static Properties getProperties() throws IOException {

        boolean isLoaded;
        if (props == null) {
            props = new Properties();
            try {
                props.load(new FileInputStream(new File(TESTPROPSFILE)));
                isLoaded = true;
            } catch (FileNotFoundException e) {
                log.debug("config_test.prop not found");
                isLoaded = false;
            }
            if (!isLoaded) {
                try {
                    props.load(new FileInputStream(new File(PROPSFILE)));
                    isLoaded = true;
                } catch (FileNotFoundException e) {
                    log.debug("config_test.prop not found");
                    isLoaded = false;
                }
            }
            if (!isLoaded) {
                log.error("Config file not found");
                System.exit(1);
            }
        }

        return props;
    }

    public static String getCallbackAddress() {
        String callbackAddress = null;
        try {
            callbackAddress = getProperties().getProperty("CallbackAddress");
        } catch (IOException e) {
            log.error("Can't read CallbackAddress. {}", e);
            System.exit(1);
        }
        return callbackAddress;
    }

    public static int getTwitchServerPort() {
        int twitchServerPort = 0;
        try {
            twitchServerPort = Integer.parseInt(getProperties().getProperty("TwitchServerPort"));
        } catch (IOException e) {
            log.error("Can't read TwitchServerPort. {}", e);
            System.exit(1);
        }
        return twitchServerPort;
    }

    public static int getManagementServerPort() {
        int managementServerPort = 0;
        try {
            managementServerPort = Integer.parseInt(getProperties().getProperty("ManagementServerPort"));
        } catch (IOException e) {
            log.error("Can't read ManagementServerPort. {}", e);
            System.exit(1);
        }
        return managementServerPort;
    }

    public static String[] getUsers() {
        String[] users = null;
        try {
            users = getProperties().getProperty("User").toLowerCase().
                    replace(" ", "").split(",");
        } catch (IOException e) {
            log.error("Can't read User. {}", e);
            System.exit(1);
        }
        return users;
    }

    public static String getRecordedStreamPath() {
        String recordedStreamPath = null;
        try {
            recordedStreamPath = getProperties().getProperty("RecordedStreamPath");
        } catch (IOException e) {
            log.error("Can't read RecordedStreamPath. {}", e);
            System.exit(1);
        }
        return recordedStreamPath;
    }

    public static boolean h2ConsoleIsEnabled() {
        boolean h2ConsoleIsEnabled = false;
        try {
            if (getProperties().getProperty("h2ConsoleIsEnabled").equals("true")) {
                h2ConsoleIsEnabled = true;
            }
        } catch (IOException e) {
            log.error("Can't read h2ConsoleIsEnabled.", e);
        }
        return h2ConsoleIsEnabled;
    }

    public static String getStreamQuality() {
        String streamQuality;
        try {
            streamQuality = getProperties().getProperty("StreamQuality");
            // check for default config
            if (streamQuality.equals("chunked, 720p60, 720p30, 480p30, 360p30, 160p30, audio_only")) {
                streamQuality = "chunked";
            }
        } catch (IOException e) {
            log.error("Can't read StreamQuality. {}", e);
            return "chunked";
        }
        return streamQuality;
    }

    public static String getMongoDBAddress() {
        String mongoDBAddress = null;
        try {
            mongoDBAddress = getProperties().getProperty("MongoDBAddress");
        } catch (IOException e) {
            log.error("Can't read MongoDBAddress. {}", e);
        }
        return mongoDBAddress;
    }

    public static String getMongoDBName() {
        String mongoDBName = null;
        try {
            mongoDBName = getProperties().getProperty("MongoDBName");
        } catch (IOException e) {
            log.error("Can't read MongoDBName. {}", e);
        }
        return mongoDBName;
    }


    public static String getH2User() {
        String h2User = null;
        try {
            if ((h2User = getProperties().getProperty("H2User")).trim().equals("")) {
                h2User = "someUser";
            }
        } catch (IOException e) {
            log.error("Can't read H2User. {}", e);
            System.exit(1);
        }
        return h2User;
    }

    public static String getH2Password() {
        String h2Password = null;
        try {
            if ((h2Password = getProperties().getProperty("H2Password")).trim().equals("")) {
                h2Password = "wy4c5j7yw457g";
            }
            ;
        } catch (IOException e) {
            log.error("Can't read H2Password. {}", e);
            System.exit(1);
        }
        return h2Password;
    }
}

