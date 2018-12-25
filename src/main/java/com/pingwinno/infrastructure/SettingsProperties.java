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

    public static String[] getUser() {
        String[] user = null;
        try {
            user = getProperties().getProperty("User").split(",");
        } catch (IOException e) {
            log.error("Can't read User. {}", e);
            System.exit(1);
        }
        return user;
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

    public static boolean getExecutePostDownloadCommand() {
        boolean executePostDownloadCommand = false;
        try {
            if (getProperties().getProperty("ExecutePostDownloadCommand").equals("true")) {
                executePostDownloadCommand = true;
            }
        } catch (IOException e) {
            log.error("Can't read ExecutePostDownloadCommand.", e);
        }
        return executePostDownloadCommand;
    }

    public static String getCommandArgs() {
        String commandArgs = null;
        try {
            commandArgs = getProperties().getProperty("CommandAgrs");
        } catch (IOException e) {
            log.error("Can't read CommandAgrs. {}", e);

        }
        return commandArgs;
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

    public static String getSqliteFile() {
        String sqliteFile = null;
        try {
            sqliteFile = getProperties().getProperty("SqliteFile");
        } catch (IOException e) {
            log.error("Can't read SqliteFile. {}", e);
            System.exit(1);
        }
        return sqliteFile;
    }
}

