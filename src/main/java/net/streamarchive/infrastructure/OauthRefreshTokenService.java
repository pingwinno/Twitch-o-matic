package net.streamarchive.infrastructure;

import net.streamarchive.application.twitch.handler.TwitchOAuthHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Timer;

@Service
public class OauthRefreshTokenService {
    private boolean isActivated;
    @Autowired
    private TwitchOAuthHandler twitchOAuthHandler;

    private Timer timer = new Timer();

    public void scheduleRefresh(int delay) {
        if (isActivated) {
            timer.cancel();
            isActivated = false;
        }
        timer.schedule(new OauthRefreshTask(twitchOAuthHandler), delay * 1000);
        isActivated = true;
    }
}
