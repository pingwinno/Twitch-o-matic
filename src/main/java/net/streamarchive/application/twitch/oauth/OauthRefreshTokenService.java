package net.streamarchive.application.twitch.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Timer;

@Service
@Slf4j
public class OauthRefreshTokenService {
    @Autowired
    private TwitchOAuthHandler twitchOAuthHandler;

    public void scheduleRefresh(int delay) {
        log.trace("Schedule timer with {} sec delay.", delay);
        Timer timer = new Timer();
        timer.schedule(new OauthRefreshTask(twitchOAuthHandler), delay * 1000);
        log.trace("Scheduled timer with {} sec delay.", delay);
    }
}
