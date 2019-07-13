package net.streamarchive.repository;

import net.streamarchive.infrastructure.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserSubscriptionsRepository extends JpaRepository<User, String> {
}
