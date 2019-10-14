package net.streamarchive.repository;

import net.streamarchive.infrastructure.models.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface StreamsRepository extends JpaRepository<Stream, UUID> {
    List<Stream> findAllByStreamer(String streamer);
}
