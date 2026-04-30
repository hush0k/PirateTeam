package com.hush0k.pirateTeam.fleet.repository;

import com.hush0k.pirateTeam.fleet.domain.Fleet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FleetRepository extends JpaRepository<Fleet, UUID> {
    Optional<Fleet> findByOwnerId(UUID ownerId);
}
