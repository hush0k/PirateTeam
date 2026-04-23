package com.hush0k.pirateTeam.team.service;

import com.hush0k.pirateTeam.exception.TeamNotFoundException;
import com.hush0k.pirateTeam.team.domain.Team;
import com.hush0k.pirateTeam.team.dto.request.TeamCreateDto;
import com.hush0k.pirateTeam.team.dto.request.TeamReputationChangeDto;
import com.hush0k.pirateTeam.team.dto.request.TeamTreasuryChangeDto;
import com.hush0k.pirateTeam.team.dto.request.TeamUpdateDto;
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
        Team team = Team.builder()
                .name(dto.name())
                .capitanId(dto.capitanId())
                .pirateIds(toPirateIds(dto.pirateIds()))
                .treasury(dto.treasury())
                .reputation(dto.reputation())
                .cohesion(dto.cohesion())
                .build();
        Team savedTeam = teamRepository.save(team);
        log.info("Team created successfully with id: {}", savedTeam.getId());
        return teamMapper.toTeamResponseDto(savedTeam);
    }

    public TeamResponseDto update(TeamUpdateDto dto, UUID id) {
        log.info("Updating team with id: {}", id);
        Team team = getExisting(id);

        if (dto.name() != null) {
            team.setName(dto.name());
        }

        if (dto.capitanId() != null) {
            team.setCapitanId(dto.capitanId());
        }

        if (dto.pirateIds() != null) {
            team.setPirateIds(toPirateIds(dto.pirateIds()));
        }

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

    public TeamResponseDto changeTreasury(UUID id, TeamTreasuryChangeDto dto) {
        log.info("Changing treasury for team with id: {}", id);
        Team team = getExisting(id);
        team.setTreasury(dto.treasury());
        Team updatedTeam = teamRepository.save(team);
        log.info("Team treasury changed successfully with id: {}", id);
        return teamMapper.toTeamResponseDto(updatedTeam);
    }

    public TeamResponseDto changeReputation(UUID id, TeamReputationChangeDto dto) {
        log.info("Changing reputation for team with id: {}", id);
        Team team = getExisting(id);
        team.setReputation(dto.reputation());
        Team updatedTeam = teamRepository.save(team);
        log.info("Team reputation changed successfully with id: {}", id);
        return teamMapper.toTeamResponseDto(updatedTeam);
    }

    private Set<UUID> toPirateIds(Set<UUID> pirateIds) {
        if (pirateIds == null) {
            return new HashSet<>();
        }

        return new HashSet<>(pirateIds);
    }
}
