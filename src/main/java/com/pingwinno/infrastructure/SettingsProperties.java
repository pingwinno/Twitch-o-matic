package com.pingwinno.infrastructure;


import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pingwinno.infrastructure.models.ConfigFile;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SettingsProperties {
    private final static String PROPSFILE = System.getProperty("user.home") + "/config.json";
    private final static String TESTPROPSFILE = "config_test.json";

    private static org.slf4j.Logger log = LoggerFactory.getLogger(SettingsProperties.class.getName());

    private static ConfigFile configFile;

    private static ConfigFile getProperties() {

        boolean isLoaded = false;
        if (configFile == null) {
            try {
                configFile = new ObjectMapper().readValue(new File(TESTPROPSFILE), ConfigFile.class);
                isLoaded = true;
            } catch (FileNotFoundException e) {
                log.debug("config_test.prop not found");
                isLoaded = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!isLoaded) {
                try {
                    configFile = new ObjectMapper().readValue(new File(PROPSFILE), ConfigFile.class);
                    isLoaded = true;
                } catch (FileNotFoundException e) {
                    log.debug("config_test.prop not found");
                    isLoaded = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!isLoaded) {
                log.error("Config file not found");
                System.exit(1);
            }
        }

        return configFile;
    }

    private static void saveProperties() {

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        log.debug("saving settings...");

        try {
            writer.writeValue(new File(TESTPROPSFILE), getProperties());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static String getCallbackAddress() {
        return getProperties().getCallbackAddress();
    }

    public static int getTwitchServerPort() {
        return getProperties().getTwitchServerPort();
    }

    public static int getManagementServerPort() {
        return getProperties().getManagementServerPort();
    }

    public static String[] getUsers() {
        return getProperties().getUsers().toArray(new String[0]);
    }

    public static void addUser(String user) {
        getProperties().getUsers().add(user);

        log.trace(getProperties().getUsers().toString());
        saveProperties();
    }

    public static void removeUser(String user) {
        getProperties().getUsers().remove(user);
        saveProperties();
    }

    public static String getRecordedStreamPath() {
        return getProperties().getRecordedStreamPath();
    }

    public static boolean h2ConsoleIsEnabled() {
        return getProperties().isH2ConsoleIsEnabled();
    }

    public static String getStreamQuality() {
        return getProperties().getStreamQuality();
    }

    public static String getMongoDBAddress() {
        return getProperties().getMongoDBAddress();
    }

    public static String getMongoDBName() {
        return getProperties().getMongoDBName();
    }


    public static String getH2User() {
        String h2User = null;
        if ((h2User = getProperties().getH2User()).trim().equals("")) {
            h2User = "someUser";
        }
        return h2User;

    }

    public static String getH2Password() {
        String h2Password = null;
        if ((h2Password = getProperties().getH2Password()).trim().equals("")) {
            h2Password = "wy4c5j7yw457g";
        }
        return h2Password;
    }

}


