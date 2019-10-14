package net.streamarchive.infrastructure;


import net.streamarchive.infrastructure.models.Streamer;
import net.streamarchive.repository.UserSubscriptionsRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SettingsProperties {

    private org.slf4j.Logger log = LoggerFactory.getLogger(SettingsProperties.class.getName());
    @Autowired
    private UserSubscriptionsRepository subscriptionsRepository;
    @Value("${net.streamarchive.security.callbackAddress}")
    private String callbackAddress;
    @Value("${net.streamarchive.security.recordStreamPath}")
    private String recordStreamPath;
    @Value("${server.port}")
    private int serverPort;
    @Value("${net.streamarchive.remoteDB.address}")
    private String remoteDBAddress;
    @Value("${net.streamarchive.remoteDB.username}")
    private String dbUsername;
    @Value("${net.streamarchive.remoteDB.password}")
    private String dbPassword;

    public String getRemoteDBAddress() {
        return remoteDBAddress;
    }

    public void setRemoteDBAddress(String remoteDBAddress) {
        this.remoteDBAddress = remoteDBAddress;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public int getTwitchServerPort() {
        return serverPort;
    }

    public String getCallbackAddress() {
        return callbackAddress;
    }


    public boolean isUserExist(String user) {
        return subscriptionsRepository.existsById(user);
    }

    public Streamer getUser(String user) {
        return subscriptionsRepository.getOne(user);
    }

    public List<Streamer> getUsers() {
        return subscriptionsRepository.findAll();
    }

    public void addUser(Streamer streamer) {
        log.debug("User {} added", streamer);
        subscriptionsRepository.saveAndFlush(streamer);
    }

    public void removeUser(String user) {
        log.debug("User {} removed", user);
        subscriptionsRepository.delete(subscriptionsRepository.getOne(user));
    }

    public String getRecordedStreamPath() {
        return recordStreamPath;
    }

}


