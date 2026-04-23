package com.hush0k.pirateTeam.team.mapper;

import com.hush0k.pirateTeam.team.domain.Team;
import com.hush0k.pirateTeam.team.dto.response.TeamResponseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper( componentModel = "spring" )
public interface TeamMapper {
    TeamResponseDto toTeamResponseDto( Team team );
    Team toTeam( TeamResponseDto teamResponseDto );
    List<TeamResponseDto> toTeamResponseDto(List<Team> teams );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTeam( TeamResponseDto teamResponseDto, @MappingTarget Team team );
}
