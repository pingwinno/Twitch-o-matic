package net.streamarchive.repository;

import net.streamarchive.infrastructure.enums.State;
import net.streamarchive.infrastructure.models.StatusDataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface StatusRepository extends JpaRepository<StatusDataModel, Integer> {
    List<StatusDataModel> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    List<StatusDataModel> findByState(State state);
}