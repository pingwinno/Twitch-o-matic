package net.streamarchive.infrastructure;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.handlers.misc.AfterApplicationStartupRunnable;
import net.streamarchive.infrastructure.models.Settings;
import net.streamarchive.infrastructure.models.Streamer;
import net.streamarchive.repository.UserSubscriptionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.restart.Restarter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static net.streamarchive.util.UrlFormatter.format;

@Slf4j
@Service
public class SettingsProvider implements AfterApplicationStartupRunnable {

    private static final String STREAMS_PATH = System.getProperty("user.home") + "/streams";
    private static final String DOCKER_SETTINGS_PATH = "/etc/streamarchive";
    private static final String STANDALONE_SETTINGS_PATH = System.getProperty("user.dir");
    private static final String SETTINGS_NAME = "settings.json";
    @Autowired
    private UserSubscriptionsRepository subscriptionsRepository;
    @Autowired
    private JsonRequestValidator dataValidator;
    private boolean settingsIsLoaded;
    private ObjectMapper mapper = new ObjectMapper();
    private Settings settings;
    private String settingsPath;
    private File settingsFile;

@PostConstruct
    private boolean init() {
        if (Files.notExists(Paths.get(DOCKER_SETTINGS_PATH))) {
            log.warn("Settings volume doesn't exist. Loading settings from working directory...");
            settingsFile = new File(format(STANDALONE_SETTINGS_PATH, SETTINGS_NAME));
            settingsPath = STANDALONE_SETTINGS_PATH;
        } else {
            log.info("Loading settings...");
            settingsFile = new File(format(DOCKER_SETTINGS_PATH, SETTINGS_NAME));
            settingsPath = DOCKER_SETTINGS_PATH;
        }
        log.trace("Settings file: {}", settingsFile);
        try {
            settings = mapper.readValue(settingsFile, Settings.class);
            settingsIsLoaded = true;
        } catch (IOException e) {
            log.warn("Can't load settings");
            settingsIsLoaded = false;
        }
        return settingsIsLoaded;
    }

    public boolean isInitialized() {
        return settingsIsLoaded;
    }

    public String getRemoteDBAddress() {
        return settings.getRemoteDBAddress();
    }

    public String getDbUsername() {
        return settings.getDbUsername();
    }

    public String getDbPassword() {
        return settings.getDbPassword();
    }

    public String getCallbackAddress() {
        return settings.getCallbackAddress();
    }

    public String getOauthRedirectAddress() {
        return settings.getOauthRedirectAddress();
    }

    public boolean isStreamerExist(String streamer) {
        return subscriptionsRepository.existsById(streamer);
    }

    public Streamer getUser(String user) {
        return subscriptionsRepository.getOne(user);
    }

    public List<Streamer> getStreamers() {
        return subscriptionsRepository.findAll();
    }

    public void addStreamer(Streamer streamer) {
        log.debug("User {} added", streamer);
        subscriptionsRepository.saveAndFlush(streamer);
    }

    public void removeUser(String user) {
        log.debug("User {} removed", user);
        subscriptionsRepository.delete(subscriptionsRepository.getOne(user));
    }

    public String getRecordedStreamPath() {
        return STREAMS_PATH;
    }

    public String getClientID() {
        return settings.getClientID();
    }

    public String getClientSecret() {
        return settings.getClientSecret();
    }

    public void saveSettings(Settings settings) throws IOException {
        dataValidator.validate(settings);
        mapper.writeValue(settingsFile, settings);
        Restarter.getInstance().restart();
    }

    public Settings getSettings() {
        return settings;
    }

    public String getUser() {
        return settings.getUser();
    }

    public void setUser(String user) {
        settings.setUser(user);
    }

    public String getPassword() {
        return settings.getUserPass();
    }

    public String getSettingsPath() {
        return settingsPath;
    }

    @Override
    public void run() {
        init();
    }
}


