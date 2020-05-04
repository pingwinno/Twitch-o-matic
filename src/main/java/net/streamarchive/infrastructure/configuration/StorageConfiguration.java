package net.streamarchive.infrastructure.configuration;

import net.streamarchive.infrastructure.data.handler.DataHandler;
import net.streamarchive.infrastructure.data.handler.FileHandler;
import net.streamarchive.infrastructure.data.handler.TelegramHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class StorageConfiguration {
    @Bean
    public DataHandler dataHandler() {
        if (Files.exists(Paths.get("tg"))) {
            return new TelegramHandler();
        } else if (Files.exists(Paths.get("file"))) {
            return new FileHandler();
        }
        return null;
    }
}
