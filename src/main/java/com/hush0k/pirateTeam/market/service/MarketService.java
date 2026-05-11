package com.hush0k.pirateTeam.market.service;

import com.hush0k.pirateTeam.exception.fleet.InsufficientPirateSpaceException;
import com.hush0k.pirateTeam.exception.pirate.PirateNotCaptainException;
import com.hush0k.pirateTeam.exception.pirate.PirateNotEnoughTreasury;
import com.hush0k.pirateTeam.exception.ship.ShipNotOwnedByThisCapitan;
import com.hush0k.pirateTeam.exception.ship.ShipNotFoundException;
import com.hush0k.pirateTeam.exception.ship.ShipUnavailableToSale;
import com.hush0k.pirateTeam.exception.team.InsufficientTreasuryException;
import com.hush0k.pirateTeam.fleet.dto.request.FleetResourceChangeDto;
import com.hush0k.pirateTeam.kafka.CapitanAssignedEvent;
import com.hush0k.pirateTeam.kafka.CargoLoadedEvent;
import com.hush0k.pirateTeam.market.client.FleetFeignClient;
import com.hush0k.pirateTeam.market.client.PirateFeignClient;
import com.hush0k.pirateTeam.market.client.ShipFeignClient;
import com.hush0k.pirateTeam.market.client.TeamFeignClient;
import com.hush0k.pirateTeam.market.client.dto.FleetClientDto;
import com.hush0k.pirateTeam.market.client.dto.FleetCreateClientDto;
import com.hush0k.pirateTeam.market.client.dto.FleetClientStatsDto;
import com.hush0k.pirateTeam.market.client.dto.PirateClientDto;
import com.hush0k.pirateTeam.market.client.dto.TeamClientDto;
import com.hush0k.pirateTeam.market.dto.response.ReceiptDto;
import com.hush0k.pirateTeam.market.kafka.MarketKafkaProducer;
import com.hush0k.pirateTeam.pirate.dto.request.PirateTreasuryChangeDto;
import com.hush0k.pirateTeam.pirate.enums.Rank;
import com.hush0k.pirateTeam.ship.domain.Ship;
import com.hush0k.pirateTeam.ship.dto.response.ShipResponseDto;
import com.hush0k.pirateTeam.ship.enums.ShipOwnership;
import com.hush0k.pirateTeam.ship.kafka.ShipKafkaProducer;
import com.hush0k.pirateTeam.ship.mapper.ShipMapper;
import com.hush0k.pirateTeam.ship.repository.ShipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MarketService {

    private final TeamFeignClient teamFeignClient;
    private final FleetFeignClient fleetFeignClient;
    private final ShipFeignClient shipFeignClient;
    private final PirateFeignClient pirateFeignClient;
    private final ShipRepository shipRepository;
    private final ShipMapper shipMapper;
    private final MarketKafkaProducer marketKafkaProducer;
    private final ShipKafkaProducer shipKafkaProducer;

    @Transactional(readOnly = true)
    private Ship getExistingShip(UUID id) {
        return shipRepository.findById(id).orElseThrow(() -> new ShipNotFoundException(id));
    }

    public ReceiptDto buyAmmo(UUID fleetId, int quantity) {
        TeamClientDto team = teamFeignClient.getByFleetId(fleetId);

        int totalPrice = quantity * 300;

        if (totalPrice > team.treasury()) {
            throw new InsufficientTreasuryException("боеприпасы", totalPrice - team.treasury());
        }

        FleetClientStatsDto fleetStats = shipFeignClient.getFleetShipStatsByFleetId(fleetId);

        int freeSpace = fleetStats.maxCargo() - fleetStats.filledCargoSpace();

        if (freeSpace < quantity * 5) {
            throw new InsufficientPirateSpaceException("боеприпаса");
        }

        fleetFeignClient.addAmmo(fleetId, new FleetResourceChangeDto(quantity));

        marketKafkaProducer.sendCargoLoaded(new CargoLoadedEvent(fleetId, quantity * 5));

        return new ReceiptDto("боеприпасы", quantity, totalPrice, true);
    }

    public ReceiptDto buyProvision(UUID fleetId, int quantity) {
        TeamClientDto team = teamFeignClient.getByFleetId(fleetId);

        int totalPrice = quantity * 150;

        if (totalPrice > team.treasury()) {
            throw new InsufficientTreasuryException("провизия", totalPrice - team.treasury());
        }

        FleetClientStatsDto fleetStats = shipFeignClient.getFleetShipStatsByFleetId(fleetId);

        int freeSpace = fleetStats.maxCargo() - fleetStats.filledCargoSpace();

        if (freeSpace < quantity * 10) {
            throw new InsufficientPirateSpaceException("провизии");
        }

        fleetFeignClient.addProvision(fleetId, new FleetResourceChangeDto(quantity));

        marketKafkaProducer.sendCargoLoaded(new CargoLoadedEvent(fleetId, quantity * 10));

        return new ReceiptDto("провизия", quantity, totalPrice, true);
    }

    @Transactional(readOnly = true)
    public List<ShipResponseDto> showMarket() {
        return shipMapper.toShipDtoList(shipRepository.getByOwnership(ShipOwnership.AVAILABLE_FOR_SALE));
    }

    public ShipResponseDto buyShip(UUID shipId, UUID captainId) {
        Ship ship = getExistingShip(shipId);

        if (ship.getOwnership() != ShipOwnership.AVAILABLE_FOR_SALE) {
            log.warn("Ship already owned by an ownership");
            throw new ShipUnavailableToSale(shipId);
        }

        PirateClientDto buyerPirate = pirateFeignClient.getPirateById(captainId);

        if (!buyerPirate.rank().isHigherThan(Rank.NAVIGATOR)) {
            throw new PirateNotCaptainException(captainId);
        }
        if (ship.getPrice() > buyerPirate.treasury()) {
            throw new PirateNotEnoughTreasury(captainId);
        }

        FleetClientDto fleet = fleetFeignClient.createFleet(
                buyerPirate.id(),
                new FleetCreateClientDto(buyerPirate.id(), "Fleet of " + buyerPirate.firstName() + " " + buyerPirate.lastName())
        );

        if (ship.getOwnerId() != null) {
            PirateClientDto sellerPirate = pirateFeignClient.getPirateById(ship.getOwnerId());
            pirateFeignClient.addPirate(sellerPirate.id(), new PirateTreasuryChangeDto(ship.getPrice()));
        }

        pirateFeignClient.withdrawPirate(captainId, new PirateTreasuryChangeDto(ship.getPrice()));

        ship.setCapitanId(captainId);
        ship.setFleetId(fleet.id());
        ship.setOwnerId(captainId);
        ship.setOwnership(ShipOwnership.OWNED);
        Ship updatedShip = shipRepository.save(ship);
        shipKafkaProducer.sendCaptainAssigned(new CapitanAssignedEvent(updatedShip.getId(), captainId));
        log.info("Корабль с ID: {} успешно продан", updatedShip.getId());
        return shipMapper.toShipDto(updatedShip);
    }

    public ShipResponseDto offerForSale(UUID shipId, UUID captainId) {
        Ship ship = getExistingShip(shipId);
        if (ship.getOwnership() != ShipOwnership.OWNED || !ship.getCapitanId().equals(captainId)) {
            log.warn("Корабль не принадлежит капитану");
            throw new ShipNotOwnedByThisCapitan(shipId, captainId);
        }

        ship.setOwnership(ShipOwnership.AVAILABLE_FOR_SALE);
        ship.setCapitanId(null);
        ship.setFleetId(null);
        Ship updatedShip = shipRepository.save(ship);
        log.info("Корабль с ID: {} успешно выставлен на продажу", updatedShip.getId());
        return shipMapper.toShipDto(updatedShip);
    }
}
