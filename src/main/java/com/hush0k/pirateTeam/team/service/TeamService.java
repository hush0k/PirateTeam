package com.hush0k.pirateTeam.team.service;

import com.hush0k.pirateTeam.exception.pirate.PirateInvalidRankException;
import com.hush0k.pirateTeam.exception.team.*;
import com.hush0k.pirateTeam.pirate.enums.Rank;
import com.hush0k.pirateTeam.team.client.PirateFeignClient;
import com.hush0k.pirateTeam.team.client.dto.CaptainClientDto;
import com.hush0k.pirateTeam.team.domain.Team;
import com.hush0k.pirateTeam.team.dto.request.*;
import com.hush0k.pirateTeam.team.dto.response.TeamResponseDto;
import com.hush0k.pirateTeam.team.mapper.TeamMapper;
import com.hush0k.pirateTeam.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@Validated
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final PirateFeignClient pirateFeignClient;

    @Transactional(readOnly = true)
    private Team getExisting(UUID id) {
        log.debug("Fetching team with id: {}", id);
        return teamRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("Team not found with id: {}", id);
                    return new TeamNotFoundException(id);
                }
        );
    }

    @Transactional(readOnly = true)
    public Team getTeam(UUID id) {
        return getExisting(id);
    }

    public TeamResponseDto create(TeamCreateDto dto) {
        log.info("Creating new team with name: {}", dto.name());
        CaptainClientDto capitan = pirateFeignClient.getPirateById(dto.capitanId());
        if (!capitan.rank().isHigherThan(Rank.COOK)) {
            log.warn("Captain with id: {} has invalid rank: {}", dto.capitanId(), capitan.rank());
            throw new PirateInvalidRankException(dto.capitanId());
        }

        Team team = teamMapper.toTeam(dto);
        team.getPirateIds().add(dto.capitanId());

        Team savedTeam = teamRepository.save(team);

        pirateFeignClient.assignManyToTeam(savedTeam.getId(), Set.of(dto.capitanId()));

        log.info("Team created successfully with id: {}", savedTeam.getId());
        return teamMapper.toTeamResponseDto(savedTeam);
    }

    public TeamResponseDto update(TeamUpdateDto dto, UUID id) {
        log.info("Updating team with id: {}", id);
        Team team = getExisting(id);
        teamMapper.updateTeam(dto, team);
        Team updatedTeam = teamRepository.save(team);
        log.info("Team updated successfully with id: {}", id);
        return teamMapper.toTeamResponseDto(updatedTeam);
    }

    public void delete(UUID id) {
        log.info("Deleting team with id: {}", id);
        Team team = getExisting(id);
        teamRepository.delete(team);
        log.info("Team deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public TeamResponseDto findById(UUID id) {
        log.debug("Fetching team by id: {}", id);
        Team team = getExisting(id);
        return teamMapper.toTeamResponseDto(team);
    }

    @Transactional(readOnly = true)
    public List<TeamResponseDto> getAll() {
        log.debug("Fetching all teams");
        List<Team> teams = teamRepository.findAll();
        log.debug("Found {} teams", teams.size());
        return teamMapper.toTeamResponseDto(teams);
    }

    // Treasury business-logic
    public TeamResponseDto addTreasury(UUID id, TeamTreasuryChangeDto dto) {
        Team team = getExisting(id);
        team.setTreasury(team.getTreasury() + dto.amount());
        Team updatedTeam = teamRepository.save(team);
        log.info("Team with id: {} added {} treasury successfully", id, dto.amount());
        return teamMapper.toTeamResponseDto(updatedTeam);
    }

    public TeamResponseDto withdrawTreasury(UUID id, TeamTreasuryChangeDto dto) {
        Team team = getExisting(id);

        if (team.getTreasury() < dto.amount()) {
            log.warn("Team with id: {} has insufficient treasury for loss of {}", id, dto.amount());
            throw new InsufficientTreasuryException(id);
        }

        team.setTreasury(team.getTreasury() - dto.amount());
        Team updatedTeam = teamRepository.save(team);
        log.info("Team with id: {} spent {} treasury successfully", id, dto.amount());
        return teamMapper.toTeamResponseDto(updatedTeam);
    }

    //Reputation business-logic
    public TeamResponseDto addReputation(UUID id, TeamReputationChangeDto dto) {
        Team team = getExisting(id);
        team.setReputation(team.getReputation() + dto.reputation());
        Team updatedTeam = teamRepository.save(team);
        log.info("Team with id: {} added {} reputation successfully", id, dto.reputation());
        return teamMapper.toTeamResponseDto(updatedTeam);
    }

    public TeamResponseDto reduceReputation(UUID id, TeamReputationChangeDto dto) {
        Team team = getExisting(id);

        if  (team.getReputation() < dto.reputation()) {
            team.setReputation(0);
        } else {
            team.setReputation(team.getReputation() - dto.reputation());
        }
        Team updatedTeam = teamRepository.save(team);
        log.info("Team with id: {} reduced {} reputation successfully", id, dto.reputation());
        return teamMapper.toTeamResponseDto(updatedTeam);
    }


    public TeamResponseDto addNewPirate(UUID id, TeamMembersChangeDto dto) {
        Team team = getExisting(id);

        Set<UUID> pirateIds = new HashSet<>(team.getPirateIds());
        Set<UUID> alreadyInTeam = findExistingPirateIds(pirateIds, dto.pirates());
        if (!alreadyInTeam.isEmpty()) {
            log.warn("Team with id: {} already contains pirates: {}", id, alreadyInTeam);
            throw new PirateAlreadyInTeamException(id, alreadyInTeam);
        }

        pirateIds.addAll(dto.pirates());
        team.setPirateIds(pirateIds);
        Team updatedTeam = teamRepository.save(team);

        pirateFeignClient.assignManyToTeam(id, dto.pirates());

        log.info("Team with id: {} added new pirates successfully: {}", id, dto.pirates());
        return teamMapper.toTeamResponseDto(updatedTeam);
    }

    public TeamResponseDto removePirate(UUID id, TeamMembersChangeDto dto) {
        Team team = getExisting(id);

        Set<UUID> pirateIds = new HashSet<>(team.getPirateIds());
        Set<UUID> missingPirates = findMissingPirateIds(pirateIds, dto.pirates());

        if (dto.pirates().contains(team.getCapitanId())) {
            log.warn("Attempt to remove captain {} from team {}", team.getCapitanId(), id);
            throw new CannotRemoveCaptainException(id, team.getCapitanId());
        }

        if (!missingPirates.isEmpty()) {
            log.warn("Team with id: {} does not contain pirates: {}", id, missingPirates);
            throw new PirateNotInTeamException(id, missingPirates);
        }

        pirateIds.removeAll(dto.pirates());
        team.setPirateIds(pirateIds);
        Team updatedTeam = teamRepository.save(team);

        pirateFeignClient.removeManyFromTeam(id, dto.pirates());

        log.info("Team with id: {} removed pirates successfully: {}", id, dto.pirates());
        return teamMapper.toTeamResponseDto(updatedTeam);
    }

    private Set<UUID> findExistingPirateIds(Set<UUID> teamPirateIds, Set<UUID> requestedPirateIds) {
        Set<UUID> existingPirateIds = new HashSet<>(requestedPirateIds);
        existingPirateIds.retainAll(teamPirateIds);
        return existingPirateIds;
    }

    private Set<UUID> findMissingPirateIds(Set<UUID> teamPirateIds, Set<UUID> requestedPirateIds) {
        Set<UUID> missingPirateIds = new HashSet<>(requestedPirateIds);
        missingPirateIds.removeAll(teamPirateIds);
        return missingPirateIds;
    }
}
