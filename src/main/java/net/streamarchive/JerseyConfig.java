package net.streamarchive;

import net.streamarchive.presentation.management.api.CorsFilter;
import net.streamarchive.presentation.management.api.ServerStatusApi;
import net.streamarchive.presentation.management.api.StreamsApi;
import net.streamarchive.presentation.management.api.SubscriprionsApi;
import net.streamarchive.presentation.twitch.api.TwitchApiHandler;
import org.glassfish.jersey.server.ResourceConfig;


public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(TwitchApiHandler.class);
        register(StreamsApi.class);
        register(ServerStatusApi.class);
        register(SubscriprionsApi.class);
        register(CorsFilter.class);
    }
}
