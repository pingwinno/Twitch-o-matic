package net.streamarchive.application.twitch.oauth;

import java.util.TimerTask;

public class OauthRefreshTask extends TimerTask {

    private final TwitchOAuthHandler twitchOAuthHandler;

    public OauthRefreshTask(TwitchOAuthHandler twitchOAuthHandler) {
        this.twitchOAuthHandler = twitchOAuthHandler;
    }

    @Override
    public void run() {
        twitchOAuthHandler.refreshAccessToken();
    }
}
