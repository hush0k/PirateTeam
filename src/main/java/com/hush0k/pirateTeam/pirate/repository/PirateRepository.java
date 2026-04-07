package com.hush0k.pirateTeam.pirate.repository;

import com.hush0k.pirateTeam.pirate.domain.Pirate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PirateRepository extends JpaRepository<Pirate, UUID> {
    Optional<Pirate> findByLogin(String login);

}
