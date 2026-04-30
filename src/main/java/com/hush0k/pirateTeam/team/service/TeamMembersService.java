package com.hush0k.pirateTeam.team.service;

import com.hush0k.pirateTeam.common.random.RandomService;
import com.hush0k.pirateTeam.exception.team.CannotRemoveCaptainException;
import com.hush0k.pirateTeam.exception.team.PirateAlreadyInTeamException;
import com.hush0k.pirateTeam.exception.team.PirateNotInTeamException;
import com.hush0k.pirateTeam.exception.team.TeamNotFoundException;
import com.hush0k.pirateTeam.team.client.PirateFeignClient;
import com.hush0k.pirateTeam.team.client.dto.CaptainClientDto;
import com.hush0k.pirateTeam.team.domain.Team;
import com.hush0k.pirateTeam.team.dto.request.TeamMembersChangeDto;
import com.hush0k.pirateTeam.team.dto.response.CoupResultResponse;
import com.hush0k.pirateTeam.team.dto.response.TeamResponseDto;
import com.hush0k.pirateTeam.team.mapper.TeamMapper;
import com.hush0k.pirateTeam.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TeamMembersService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final PirateFeignClient pirateFeignClient;
    private final RandomService randomService;


    // Team membership operations
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

    public CoupResultResponse startCoup(UUID rebelId, UUID teamId) {
        Team team = getExisting(teamId);

        if (!team.getPirateIds().contains(rebelId)) {
            log.warn("Pirate with id: {} is not a member of team {}", rebelId, teamId);
            throw new PirateNotInTeamException(teamId, Set.of(rebelId));
        }

        if (rebelId.equals(team.getCapitanId())) {
            throw new IllegalArgumentException("Капитан не может устроить переворот против себя");
        }

        CaptainClientDto rebel = pirateFeignClient.getPirateById(rebelId);
        CaptainClientDto captain = pirateFeignClient.getPirateById(team.getCapitanId());


        int reputationDifference = rebel.reputation() - captain.reputation();
        int loyaltyPenalty = team.getLoyalty() - 50;

        int center = randomService.clamp(
                50 + reputationDifference - loyaltyPenalty,
                0,
                100
        );
        int result = randomService.weightedAround(0, 100, center);
        LocalDateTime now = LocalDateTime.now();

        if (result < 80) {
            log.info("Coup failed for rebel {} in team {}. Result: {}", rebelId, teamId, result);
            return new CoupResultResponse("failure", result + "%", now);
        } else {
            log.info("Coup succeeded for rebel {} in team {}. Result: {}", rebelId, teamId, result);
            team.setCapitanId(rebelId);
            team.getPirateIds().remove(captain.id());
            pirateFeignClient.removeManyFromTeam(teamId, Set.of(captain.id()));
            teamRepository.save(team);
            return new CoupResultResponse("success", result + "%", now);
        }
    }


    // Membership set helpers
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

    // Shared entity lookup
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

    //
}
