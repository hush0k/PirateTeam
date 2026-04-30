package com.hush0k.pirateTeam.team.repository;

import com.hush0k.pirateTeam.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
    Optional<Team> findByFleetId(UUID fleetId);
}
