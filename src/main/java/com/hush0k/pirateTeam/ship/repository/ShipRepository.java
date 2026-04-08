package com.hush0k.pirateTeam.ship.repository;

import com.hush0k.pirateTeam.ship.domain.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShipRepository extends JpaRepository<Ship, UUID> {
}
