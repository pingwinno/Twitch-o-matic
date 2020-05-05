package net.streamarchive.infrastructure.configuration;

import net.streamarchive.infrastructure.data.handler.FileStorageService;
import net.streamarchive.infrastructure.data.handler.StorageService;
import net.streamarchive.infrastructure.data.handler.TelegramStorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class StorageConfiguration {
    @Bean
    public StorageService dataHandler() {
        if (Files.exists(Paths.get("tg"))) {
            return new TelegramStorageService();
        } else if (Files.exists(Paths.get("file"))) {
            return new FileStorageService();
        }
        return null;
    }
}
