package com.pingwinno.infrastructure;


import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SettingsProperties {
    private final static String PROPSFILE = "config.prop";
    private static org.slf4j.Logger log = LoggerFactory.getLogger(SettingsProperties.class.getName());

    private static Properties props;

    private static Properties getProperties() throws IOException {

        try {

            if (props == null) {
                props = new Properties();
                props.load(new FileInputStream(new File(PROPSFILE)));
            }
        } catch (FileNotFoundException e) {
            log.error("config.prop not found. {}", e);
            log.error("Place config.prop in Twitch-o-matic folder and try again");
            System.exit(1);
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

    public static String getUser() {
        String user = null;
        try {
            user = getProperties().getProperty("User");
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
            System.exit(1);
        }
        return executePostDownloadCommand;
    }

    public static String getCommandArgs() {
        String commandArgs = null;
        try {
            commandArgs = getProperties().getProperty("CommandAgrs");
        } catch (IOException e) {
            log.error("Can't read CommandAgrs. {}", e);
            System.exit(1);
        }
        return commandArgs;
    }

    public static String getStreamQuality() {
        String streamQuality = null;
        try {
            streamQuality = getProperties().getProperty("StreamQuality");
        } catch (IOException e) {
            log.error("Can't read StreamQuality. {}", e);
            System.exit(1);
        }
        return streamQuality;
    }

    public static String getRedisPutEndpoint() {
        String redisPutEndpoint = null;
        try {
            redisPutEndpoint = getProperties().getProperty("RedisPutEndpoint");
        } catch (IOException e) {
            log.error("Can't read RedisPutEndpoint. {}", e);
            System.exit(1);
        }
        return redisPutEndpoint;
    }
}

