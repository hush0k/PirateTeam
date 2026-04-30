package com.hush0k.pirateTeam.ship.repository;

import com.hush0k.pirateTeam.ship.domain.Ship;
import com.hush0k.pirateTeam.ship.dto.response.ShipResponseDto;
import com.hush0k.pirateTeam.ship.enums.ShipOwnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShipRepository extends JpaRepository<Ship, UUID> {

    List<Ship> getByFleetId(UUID fleetId);
    List<Ship> getByOwnership(ShipOwnership shipOwnership);


}
