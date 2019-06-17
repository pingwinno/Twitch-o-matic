package net.streamarchive;

import net.streamarchive.presentation.management.api.*;
import net.streamarchive.presentation.twitch.api.TwitchApiHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(TwitchApiHandler.class);
        register(SiteHandler.class);
        register(StatusApiHandler.class);
        register(StreamsApi.class);
        register(ServerStatusApi.class);
        register(SubscriprionsApi.class);
        register(CorsFilter.class);
    }




}
