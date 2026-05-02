package com.hush0k.pirateTeam.pirate.service;

import com.hush0k.pirateTeam.exception.pirate.InsufficientExpForUpgradeException;
import com.hush0k.pirateTeam.fleet.service.FleetService;
import com.hush0k.pirateTeam.pirate.domain.Pirate;
import com.hush0k.pirateTeam.pirate.dto.request.PirateExpChangeDto;
import com.hush0k.pirateTeam.pirate.dto.response.PirateResponseDto;
import com.hush0k.pirateTeam.pirate.mapper.PirateMapper;
import com.hush0k.pirateTeam.pirate.repository.PirateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PirateGamePlayService {

    private final PirateService pirateService;
    private final PirateRepository  pirateRepository;
    private final PirateMapper pirateMapper;

    public PirateResponseDto addExp(UUID id, PirateExpChangeDto dto) {
        log.info("Adding {} exp to pirate {}", dto.amount(), id);
        Pirate pirate = pirateService.getExisting(id);
        pirate.setExp(pirate.getExp() + dto.amount());
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate exp increased successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto removeExp(UUID id, PirateExpChangeDto dto) {
        log.info("Removing {} exp from pirate {}", dto.amount(), id);
        Pirate pirate = pirateService.getExisting(id);
        pirate.setExp(Math.max(0, pirate.getExp() - dto.amount()));
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate exp reduced successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto upgradeBloodlust(UUID id, int amount){
        Pirate pirate = pirateService.getExisting(id);

        int neededExp = amount * 10;
        if (pirate.getExp() < neededExp){
            throw new InsufficientExpForUpgradeException(id, neededExp);
        }

        pirate.setExp(pirate.getExp() - neededExp);
        pirate.setBloodlust(pirate.getBloodlust() + amount);

        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate exp increased successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto upgradeIntelligence(UUID id, int amount){
        Pirate pirate = pirateService.getExisting(id);

        int neededExp = amount * 10;
        if (pirate.getExp() < neededExp){
            throw new InsufficientExpForUpgradeException(id, neededExp);
        }

        pirate.setExp(pirate.getExp() - neededExp);
        pirate.setIntelligence(pirate.getIntelligence() + amount);

        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate exp increased successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto upgradeStrength(UUID id, int amount){
        Pirate pirate = pirateService.getExisting(id);

        int neededExp = amount * 10;
        if (pirate.getExp() < neededExp){
            throw new InsufficientExpForUpgradeException(id, neededExp);
        }

        pirate.setExp(pirate.getExp() - neededExp);
        pirate.setStrength(pirate.getStrength() + amount);
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate exp increased successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto upgradeLevel(UUID id) {
        Pirate pirate = pirateService.getExisting(id);

        if (pirate.getExp() < 300){
            throw new InsufficientExpForUpgradeException(id, 300);
        }

        pirate.setExp(pirate.getExp() - 300);
        pirate.setRank(pirate.getRank().next());
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate rank upgraded successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }


}
