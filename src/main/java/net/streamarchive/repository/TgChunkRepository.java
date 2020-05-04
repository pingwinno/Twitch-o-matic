package net.streamarchive.repository;

import net.streamarchive.infrastructure.models.TgChunk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TgChunkRepository extends JpaRepository<TgChunk, Integer> {
    TgChunk findByUuidAndStreamerAndChunkName(UUID uuid, String streamer, String chunkName);

    List<TgChunk> findByUuidAndStreamer(UUID uuid, String streamer);

    boolean existsByUuid(UUID uuid);
}
