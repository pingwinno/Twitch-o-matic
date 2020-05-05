package net.streamarchive.infrastructure;

import net.streamarchive.application.twitch.handler.TwitchOAuthHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;

@Service
public class OauthRefreshTokenTask extends TimerTask {

    @Autowired
    private TwitchOAuthHandler twitchOAuthHandler;

    public void scheduleRefresh(int delay) {
        new Timer().schedule(this, delay * 1000);
    }

    @Override
    public void run() {
        twitchOAuthHandler.refreshAccessToken();
    }
}
