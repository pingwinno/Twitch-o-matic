package net.streamarchive.infrastructure;


import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.streamarchive.infrastructure.models.ConfigFile;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class SettingsProperties {
    private final String PROPSFILE = System.getProperty("user.home") + "/config.json";
    private final String TESTPROPSFILE = "config_test.json";

    private org.slf4j.Logger log = LoggerFactory.getLogger(SettingsProperties.class.getName());

    private ConfigFile configFile;

    private ConfigFile getProperties() {

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

    private void saveProperties() {

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        log.debug("saving settings...");

        try {
            writer.writeValue(new File(TESTPROPSFILE), getProperties());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String getCallbackAddress() {
        return getProperties().getCallbackAddress();
    }

    public int getTwitchServerPort() {
        return getProperties().getTwitchServerPort();
    }

    public int getManagementServerPort() {
        return getProperties().getManagementServerPort();
    }

    public Map<String, List<String>> getUsers() {
        return getProperties().getUsers();
    }

    public void addUser(Map<String, List<String>> users) {
        getProperties().getUsers().putAll(users);

        log.trace(getProperties().getUsers().toString());
        saveProperties();
    }

    public void removeUser(String user) {
        getProperties().getUsers().remove(user);
        saveProperties();
    }

    public String getRecordedStreamPath() {
        return getProperties().getRecordedStreamPath();
    }

    public boolean h2ConsoleIsEnabled() {
        return getProperties().isH2ConsoleIsEnabled();
    }

    public String getStreamQuality() {
        return getProperties().getStreamQuality();
    }

    public String getMongoDBAddress() {
        return getProperties().getMongoDBAddress();
    }

    public String getMongoDBName() {
        return getProperties().getMongoDBName();
    }


    public String getH2User() {
        String h2User = null;
        if ((h2User = getProperties().getH2User()).trim().equals("")) {
            h2User = "someUser";
        }
        return h2User;

    }

    public String getH2Password() {
        String h2Password = null;
        if ((h2Password = getProperties().getH2Password()).trim().equals("")) {
            h2Password = "wy4c5j7yw457g";
        }
        return h2Password;
    }

}


