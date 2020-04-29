package net.streamarchive.infrastructure;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.models.Settings;
import net.streamarchive.infrastructure.models.Streamer;
import net.streamarchive.repository.UserSubscriptionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@Order(1)
public class SettingsProperties {

    @Autowired
    private UserSubscriptionsRepository subscriptionsRepository;

    private boolean settingsIsLoaded;
    private ObjectMapper mapper = new ObjectMapper();
    private Settings settings;
    private File settingsFile = new File("settings.json");

    @PostConstruct
    private boolean init() {
        try {
            settings = mapper.readValue(settingsFile, Settings.class);
            settingsIsLoaded = true;
        } catch (IOException e) {
            log.warn("Can't load settings");
            settingsIsLoaded = false;
        }
        return settingsIsLoaded;
    }

    public boolean isInitialized(){
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
        return settings.getRecordStreamPath();
    }

    public String getClientID() {
        return settings.getClientID();
    }

    public void saveSettings(Settings settings) throws IOException {
        mapper.writeValue(settingsFile, settings);
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
}


