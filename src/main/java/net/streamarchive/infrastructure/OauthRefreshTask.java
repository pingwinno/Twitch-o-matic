package net.streamarchive.infrastructure;

import net.streamarchive.application.twitch.handler.TwitchOAuthHandler;

import java.util.TimerTask;

public class OauthRefreshTask extends TimerTask {

    private TwitchOAuthHandler twitchOAuthHandler;

    public OauthRefreshTask(TwitchOAuthHandler twitchOAuthHandler) {
        this.twitchOAuthHandler = twitchOAuthHandler;
    }

    @Override
    public void run() {
        twitchOAuthHandler.refreshAccessToken();
    }
}
