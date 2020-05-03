package net.streamarchive.infrastructure.configuration;

import net.streamarchive.infrastructure.data.handler.DataHandler;
import net.streamarchive.infrastructure.data.handler.TelegramHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {
    @Bean
    public DataHandler dataHandler() {
        return new TelegramHandler();
    }
}
