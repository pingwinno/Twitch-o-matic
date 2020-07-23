package net.streamarchive.repository;

import net.streamarchive.infrastructure.models.TelegramFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TgChunkRepository extends JpaRepository<TelegramFile, Long> {
    TelegramFile findByPath(String path);
}
