package net.streamarchive.repository;

import net.streamarchive.infrastructure.models.Streamer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserSubscriptionsRepository extends JpaRepository<Streamer, String> {
}
