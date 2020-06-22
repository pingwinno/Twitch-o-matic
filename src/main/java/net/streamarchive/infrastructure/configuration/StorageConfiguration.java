package net.streamarchive.infrastructure.configuration;

import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.data.handler.FileStorageService;
import net.streamarchive.infrastructure.data.handler.StorageService;
import net.streamarchive.infrastructure.data.handler.TelegramStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class StorageConfiguration {
    @Value("${net.streamarchive.storage}")
    private String storageType;

    @Bean
    public StorageService dataHandler() {
        log.trace("Storage type is {}", storageType);
        if ("telegram".equals(storageType)) {
            return new TelegramStorageService();
        } else if ("file".equals(storageType)) {
            return new FileStorageService();
        }
        throw new IllegalArgumentException("Storage type must be telegram or file");
    }
}
