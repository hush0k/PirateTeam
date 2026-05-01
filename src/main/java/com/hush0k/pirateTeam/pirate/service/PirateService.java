package com.hush0k.pirateTeam.pirate.service;

import com.hush0k.pirateTeam.exception.pirate.InsufficientPirateTreasuryException;
import com.hush0k.pirateTeam.exception.pirate.PirateNotFoundException;
import com.hush0k.pirateTeam.pirate.domain.Pirate;
import com.hush0k.pirateTeam.pirate.dto.request.*;
import com.hush0k.pirateTeam.pirate.dto.response.PirateResponseDto;
import com.hush0k.pirateTeam.pirate.enums.Rank;
import com.hush0k.pirateTeam.pirate.mapper.PirateMapper;
import com.hush0k.pirateTeam.pirate.repository.PirateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PirateService {

    private static final int MAX_CHARACTERISTIC_VALUE = 100;

    private final PirateRepository pirateRepository;
    private final PirateMapper pirateMapper;
    private final PasswordEncoder passwordEncoder;

    // EXISTS
    @Transactional(readOnly = true)
    private Pirate getExisting(UUID pirateId) {
        log.debug("Fetching pirate with id: {}", pirateId);
        return pirateRepository.findById(pirateId).orElseThrow(
                () -> {
                    log.warn("Pirate not found with id: {}", pirateId);
                    return new PirateNotFoundException(pirateId);
                }
        );
    }

    // CREATE
    public PirateResponseDto create(PirateCreateDto dto) {
        log.info("Creating new pirate with name: {}", dto.firstName());
        Pirate pirate = pirateMapper.toPirate(dto);
        pirate.setPassword(passwordEncoder.encode(dto.password()));
        Pirate savedPirate = pirateRepository.save(pirate);
        log.info("Pirate created successfully with id: {}", savedPirate.getId());
        return pirateMapper.toPirateResponseDto(savedPirate);
    }

    // UPDATE
    public PirateResponseDto update(PirateUpdateDto dto, UUID id) {
        log.info("Updating pirate with name: {}", dto.firstName());
        Pirate pirate = getExisting(id);
        pirateMapper.updatePirateDto(dto, pirate);
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate updated successfully with name: {}", dto.firstName());
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    // DELETE
    public void delete(UUID id) {
        log.info("Deleting pirate with id: {}", id);
        Pirate pirate = getExisting(id);
        pirateRepository.delete(pirate);
        log.info("Pirate deleted successfully with id: {}", id);
    }

    // GET
    @Transactional(readOnly = true)
    public PirateResponseDto findById(UUID id) {
        log.debug("Fetching pirate by id: {}", id);
        Pirate pirate = getExisting(id);
        return pirateMapper.toPirateResponseDto(pirate);
    }

    @Transactional(readOnly = true)
    public List<PirateResponseDto> getAll() {
        log.debug("Fetching all pirates");
        List<Pirate> pirates = pirateRepository.findAll();
        log.debug("Found {} pirates", pirates.size());
        return pirateMapper.toPirateAllResponseDto(pirates);
    }

    // BUSINESS LOGIC
    public PirateResponseDto changeRank(UUID id, Rank rank) {
        log.info("Changing rank for pirate with id: {} to rank: {}", id, rank);
        Pirate pirate = getExisting(id);
        Rank previousRank = pirate.getRank();
        pirate.setRank(rank);
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate rank changed successfully: {} -> {} for pirate id: {}", previousRank, rank, id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public void assignManyToTeam(Set<UUID> pirateId, UUID teamId){
        List<Pirate> pirates = pirateRepository.findAllById(pirateId);

        if(pirates.size() != pirateId.size()){
            Set<UUID> foundIds = pirates.stream().map(Pirate::getId).collect(Collectors.toSet());
            Set<UUID> missing = new HashSet<>(pirateId);
            missing.removeAll(foundIds);
            log.info("Missing pirate ids found: {}", missing);
            throw new PirateNotFoundException(missing.iterator().next());
        }

        pirates.forEach(pirate -> pirate.setTeamId(teamId));
        pirateRepository.saveAll(pirates);
        log.info("Assigned {} pirates to team {} successfully", pirates.size(), teamId);
    }

    public void removeManyFromTeam(Set<UUID> pirateIds, UUID teamId){
        log.info("Removing {} pirates from team {}", pirateIds.size(), teamId);
        List<Pirate> pirates = pirateRepository.findAllById(pirateIds);
        pirates.forEach(p -> p.setTeamId(null));
        pirateRepository.saveAll(pirates);
        log.info("Removed {} pirates from team {} successfully", pirates.size(), teamId);
    }


    public PirateResponseDto addReputation(UUID id, PirateReputationChange dto) {
        log.info("Adding reputation {} to pirate with id: {}", dto.reputation(), id);
        Pirate pirate = getExisting(id);
        pirate.setReputation(capCharacteristic(pirate.getReputation() + dto.reputation()));
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate reputation changed successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto removeReputation(UUID id, PirateReputationChange dto) {
        log.info("Removing reputation {} from team {}", dto.reputation(), id);
        Pirate pirate = getExisting(id);
        pirate.setReputation(Math.max(0, pirate.getReputation() - dto.reputation()));
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate reputation changed successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto addTreasury(UUID id, PirateTreasuryChangeDto dto) {
        log.info("Adding {} treasury to pirate {}", dto.amount(), id);
        Pirate pirate = getExisting(id);
        pirate.setTreasury(pirate.getTreasury() + dto.amount());
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate treasury changed successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto withdrawTreasury(UUID id, PirateTreasuryChangeDto dto) {
        log.info("Withdrawing {} treasury from pirate {}", dto.amount(), id);
        Pirate pirate = getExisting(id);
        if (pirate.getTreasury() < dto.amount()) {
            log.warn("Pirate with id: {} has insufficient treasury for withdraw of {}", id, dto.amount());
            throw new InsufficientPirateTreasuryException(id);
        }
        pirate.setTreasury(pirate.getTreasury() - dto.amount());
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate treasury changed successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto addBloodlust(UUID id, PirateBloodlustChangeDto dto) {
        log.info("Adding {} bloodlust to pirate {}", dto.amount(), id);
        Pirate pirate = getExisting(id);
        pirate.setBloodlust(capCharacteristic(pirate.getBloodlust() + dto.amount()));
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate bloodlust changed successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto removeBloodlust(UUID id, PirateBloodlustChangeDto dto) {
        log.info("Removing {} bloodlust from pirate {}", dto.amount(), id);
        Pirate pirate = getExisting(id);
        pirate.setBloodlust(Math.max(0, pirate.getBloodlust() - dto.amount()));
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate bloodlust changed successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto addIntelligence(UUID id, PirateIntelligenceChangeDto dto) {
        log.info("Adding {} intelligence to pirate {}", dto.amount(), id);
        Pirate pirate = getExisting(id);
        pirate.setIntelligence(capCharacteristic(pirate.getIntelligence() + dto.amount()));
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate intelligence changed successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto removeIntelligence(UUID id, PirateIntelligenceChangeDto dto) {
        log.info("Removing {} intelligence from pirate {}", dto.amount(), id);
        Pirate pirate = getExisting(id);
        pirate.setIntelligence(Math.max(0, pirate.getIntelligence() - dto.amount()));
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate intelligence changed successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto addStrength(UUID id, PirateStrengthChangeDto dto) {
        log.info("Adding {} strength to pirate {}", dto.amount(), id);
        Pirate pirate = getExisting(id);
        pirate.setStrength(capCharacteristic(pirate.getStrength() + dto.amount()));
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate strength changed successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto removeStrength(UUID id, PirateStrengthChangeDto dto) {
        log.info("Removing {} strength from pirate {}", dto.amount(), id);
        Pirate pirate = getExisting(id);
        pirate.setStrength(Math.max(0, pirate.getStrength() - dto.amount()));
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate strength changed successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto addExp(UUID id, PirateExpChangeDto dto) {
        log.info("Adding {} exp to pirate {}", dto.amount(), id);
        Pirate pirate = getExisting(id);
        pirate.setExp(pirate.getExp() + dto.amount());
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate exp increased successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto removeExp(UUID id, PirateExpChangeDto dto) {
        log.info("Removing {} exp from pirate {}", dto.amount(), id);
        Pirate pirate = getExisting(id);
        pirate.setExp(Math.max(0, pirate.getExp() - dto.amount()));
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate exp reduced successfully for pirate id: {}", id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    private int capCharacteristic(int value) {
        return Math.min(MAX_CHARACTERISTIC_VALUE, value);
    }

}
