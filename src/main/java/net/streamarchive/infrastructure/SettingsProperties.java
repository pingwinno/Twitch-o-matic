package net.streamarchive.infrastructure;


import net.streamarchive.infrastructure.models.User;
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

    public int getTwitchServerPort() {
        return serverPort;
    }

    public String getCallbackAddress() {
        return callbackAddress;
    }


    public boolean isUserExist(String user) {
        return subscriptionsRepository.existsById(user);
    }

    public User getUser(String user) {
        return subscriptionsRepository.getOne(user);
    }

    public List<User> getUsers() {
        return subscriptionsRepository.findAll();
    }

    public void addUser(User user) {
        log.debug("User {} added", user);
        subscriptionsRepository.saveAndFlush(user);
    }

    public void removeUser(String user) {
        log.debug("User {} removed", user);
        subscriptionsRepository.delete(subscriptionsRepository.getOne(user));
    }

    public String getRecordedStreamPath() {
        return recordStreamPath;
    }


}


