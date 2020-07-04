package net.streamarchive.presentation.twitch.api;

import net.streamarchive.application.twitch.oauth.TwitchOAuthHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/oauth")
public class TwitchOauthApi {
    @Autowired
    private TwitchOAuthHandler twitchOAuthHandler;

    @GetMapping
    public void receiveAuthorizationToken(@RequestParam("code") String authorizationCode, @RequestParam("scope") String scope) {
        twitchOAuthHandler.requestAccessToken(authorizationCode);
    }
}
