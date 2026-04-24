package com.hush0k.pirateTeam.island.repository;

import com.hush0k.pirateTeam.island.domain.Island;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IslandRepository extends JpaRepository<Island, UUID> {
}
